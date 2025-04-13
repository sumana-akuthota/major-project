from health.firebaseservice import create_record, get_records, delete_record, get_record_by_id, update_record
from .forms import LoginForm
from firebase_admin import db
from django.shortcuts import render, redirect

def login(request):
    if request.method == "GET":
        loginForm = LoginForm(request.GET)
        if loginForm.is_valid():
            uname = loginForm.cleaned_data["username"]
            upass = loginForm.cleaned_data["password"]
            print(uname, upass)
            if uname == "admin" and upass == "admin":
                request.session['username'] = "admin"
                request.session['role'] = "admin"
                print("admin")
                records = get_records("hospitals")
                print(records)
                return render(request, 'viewhospitals.html', {'hospitals': records})
            else:
                print("invalid form")
                return render(request, 'index.html', {"message": "Invalid Credentials"})
        else:
            print("invalid form")
            return render(request, 'index.html', {"message": "Invalid Form"})
    else:
        print("invalid request")
        return render(request, 'index.html', {"message": "Invalid Request"})

def logout(request):
    try:
        del request.session['username']
    except:
        pass
    return render(request, "index.html")
#-----------------------------------------------------------------------------------------------------


from firebase_admin import db
from django.shortcuts import render

def view_details(request):
    hospitals_data = db.reference('hospitals').get() or {}
    policies_data = db.reference('healthpolicies').get() or {}
    claims_data = db.reference('claimrequests').get() or {}
    patients_data = db.reference('patients').get() or {} # Fetch the patients node

    # Initialize counts
    approved_count = 0
    pending_count = 0

    # Prepare claim request summary
    enriched_claims = []
    for claim_id, claim in claims_data.items():
        health_card_number_from_claim = claim.get('patientAadhaarNo') # This is the health card number from claim
        policy_id = claim.get('policyId')
        hospital_id = claim.get('hospitalId')
        patient_name = 'N/A'
        patient_contact = 'N/A'
        actual_aadhaar_no = 'N/A'
        health_card_number_patient_key = 'N/A' # Initialize health card number key

        # Retrieve patient information using the health card number as the key
        if health_card_number_from_claim and health_card_number_from_claim in patients_data:
            patient_info = patients_data[health_card_number_from_claim]
            patient_name = patient_info.get('name', 'N/A')
            patient_contact = patient_info.get('contactNo', 'N/A')
            actual_aadhaar_no = patient_info.get('aadhaarNo', 'N/A')
            health_card_number_patient_key = health_card_number_from_claim # The key is the health card number

        hospital = hospitals_data.get(hospital_id)
        policy = policies_data.get(policy_id)

        hospital_name = hospital.get('name', 'N/A') if hospital else 'N/A'
        hospital_contact = hospital.get('contactNo', 'N/A') if hospital else 'N/A'
        policy_name = policy.get('policyName', 'N/A') if policy else 'N/A'
        policy_description = policy.get('description', 'N/A') if policy else 'N/A'
        coverage_amount = policy.get('coverageAmount', 'N/A') if policy else 'N/A'

        # Count status
        status = claim.get('status', '').lower()
        if status == 'approved':
            approved_count += 1
        elif status == 'pending':
            pending_count += 1

        enriched_claims.append({
            'health_card_number_claim': health_card_number_from_claim, # Health card number from claim
            'patient_name': patient_name,
            'patient_contact': patient_contact,
            'status': claim.get('status', 'N/A'),
            'hospital_name': hospital_name,
            'hospital_contact': hospital_contact,
            'policy_name': policy_name,
            'policy_description': policy_description,
            'coverage_amount': coverage_amount,
            'aadhar_no': actual_aadhaar_no, # Actual Aadhaar number from patients
            'health_card_number_patient': health_card_number_patient_key, # Health card number (key in patients)
        })

    return render(request, 'view_details.html', {
        'claims_summary': enriched_claims,
        'approved_count': approved_count,
        'pending_count': pending_count,
    })
#======================================================================================
from django.shortcuts import render
from firebase_admin import db
import json
from django.http import JsonResponse

def view_patients(request):
    # ... (your existing view_patients function code - no changes needed here) ...
    patients_ref = db.reference('patients')
    patients_data = patients_ref.get()
    family_ref = db.reference('PatientsFamily')
    family_data = family_ref.get()
    claims_ref = db.reference('claimrequests')
    claims_data = claims_ref.get() or {}

    search_query = request.GET.get('q', '').strip().upper()



    patients_with_family = {}
    if patients_data:
        for healthcard, patient_info in patients_data.items():
            family_details = family_data.get(healthcard, {}).get('familyDetails', {})
            family_member1 = family_details.get('familyMember1', {})
            family_member2 = family_details.get('familyMember2', {})

            patient_name = patient_info.get('name', '').upper()
            healthcard_upper = healthcard.upper()

            policies_applied_count = 0
            policies_approved_count = 0


            for claim_id, claim_data in claims_data.items():
                mapped_healthcard = claim_data.get('patientAadhaarNo')
                policy_status = claim_data.get('status', '').lower()



                if mapped_healthcard == healthcard:
                    policies_applied_count += 1
                    if policy_status == 'approved':
                        policies_approved_count += 1



            if search_query in patient_name or search_query in healthcard_upper or not search_query:
                patients_with_family[healthcard] = {
                    'patient': patient_info,
                    'familyMember1': family_member1,
                    'familyMember2': family_member2,
                    'policies_applied_count': policies_applied_count,
                    'policies_approved_count': policies_approved_count,
                }


    return render(request, 'viewpatients.html', {'patients_with_family': patients_with_family, 'search_query': request.GET.get('q', '')})


def get_patient_policies_hospitals(request):
    healthcard = request.GET.get('healthcard')
    if not healthcard:
        return JsonResponse({'error': 'Healthcard is required'}, status=400)

    try:
        claims_ref = db.reference('claimrequests')
        claims_data = claims_ref.get() or {}
        policies_ref = db.reference('healthpolicies')
        policies_data = policies_ref.get() or {}
        hospitals_ref = db.reference('hospitals')
        hospitals_data = hospitals_ref.get() or {}

        patient_claims = [claim for claim in claims_data.values() if claim.get('patientAadhaarNo') == healthcard]

        policy_hospital_data = []

        for claim in patient_claims:
            policy_id = claim.get('policyId')
            hospital_id = claim.get('hospitalId')
            policy_status = claim.get('status', '').lower()
            # Get amountApproved, no default.
            amount_approved = claim.get('amountApproved')
            # Get coverage amount from policies data
            coverage_amount = policies_data.get(policy_id, {}).get('coverageAmount')
            
            policy_name = policies_data.get(policy_id, {}).get('policyName', 'Unknown Policy')
            hospital_name = hospitals_data.get(hospital_id, {}).get('name', 'Unknown Hospital')
            
            if policy_status != 'approved':
                amount_approved = 'Pending'
            elif amount_approved is None:  # if amount_approved is null or none
                amount_approved = coverage_amount if coverage_amount is not None else 'Pending'

            policy_hospital_data.append({
                'policy_name': policy_name,
                'hospital_name': hospital_name,
                'amount_approved': amount_approved if amount_approved else 'Pending',
            })
        return JsonResponse(policy_hospital_data, safe=False)

    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)


#======================================================================================

def add_health_policy(request):
    record = {
        "coverageAmount": request.GET["coverageAmount"],
        "description": request.GET["description"],
        "policyName": request.GET["policyName"]
    }
    create_record("healthpolicies", record)

    #============================================================================
    records = get_records("healthpolicies")
    print(records)
    return render(request, 'viewhealthpolicies.html', {'healthpolicies': records})

def view_health_policies(request):
    records = get_records("healthpolicies")
    print(records)
    return render(request, 'viewhealthpolicies.html', {'healthpolicies': records})

def delete_health_policy(request):
    record_id = request.GET['id']
    delete_record("healthpolicies", record_id)

    records = get_records("healthpolicies")
    print(records)
    return render(request, 'viewhealthpolicies.html', {'healthpolicies': records})

#======================================================================================
def view_hospitals(request):
    records = get_records("hospitals")
    print(records)
    return render(request, 'viewhospitals.html', {'hospitals': records})


def update_hospital(request):
    """Handle hospital activation/deactivation."""
    try:
        hospital_id = request.GET.get("id")
        active_status = request.GET.get("active")

        if not hospital_id or active_status is None:
            message = "Missing required parameters (id, active)."

        # Convert active_status string to boolean
        active_bool = active_status.lower() == "true"

        # Retrieve hospital record
        record = get_record_by_id("hospitals", hospital_id)
        if not record:
            message = f"Hospital with ID {hospital_id} not found."

        # Update the record
        record["active"] = active_bool
        update_record("hospitals", hospital_id, record)

        # Retrieve updated hospital list
        hospitals = get_records("hospitals")

        # Redirect to view hospitals page
        return render(request, 'viewhospitals.html', {'hospitals': hospitals})

    except Exception as e:
        message="An unexpected error occurred."

    records = get_records("hospitals")

    return render(request, 'viewhospitals.html',
                        {'hospitals': records, "message": message})

#======================================================================================

from health.firebaseservice import get_records, get_record_by_id, update_record
from firebase_admin import db
from django.shortcuts import render

def view_claimrequests(request):
    claim_requests_data = get_records("claimrequests")
    patient_documents_data = db.reference('patient_documents').get() or {}
    patients_data = db.reference('patients').get() or {} # Fetch patients data
    print("Claim Requests:", claim_requests_data)
    print("Patient Documents:", patient_documents_data)
    print("Patients Data:", patients_data)

    claim_requests_with_details = []
    for record_id, claim in claim_requests_data.items():
        health_card_number = claim.get('patientAadhaarNo') # Health card number from claim
        documents = []
        patient_name = 'N/A'
        actual_aadhaar_no = 'N/A'

        if health_card_number and health_card_number in patients_data:
            patient_info = patients_data[health_card_number]
            patient_name = patient_info.get('name', 'N/A')
            actual_aadhaar_no = patient_info.get('aadhaarNo', 'N/A')

            # Find documents for the patient (using the health card number as patientId)
            for doc_id, doc_info in patient_documents_data.items():
                if doc_info.get('patientId') == health_card_number:
                    file_url = doc_info.get('fileUrl')
                    if file_url:
                        documents.append(file_url)

        claim_with_details = claim.copy()
        claim_with_details['documents'] = documents
        claim_with_details['patient_name'] = patient_name
        claim_with_details['actual_aadhaar_no'] = actual_aadhaar_no
        claim_requests_with_details.append((record_id, claim_with_details))

    print("Claim Requests with Details:", claim_requests_with_details)
    return render(request, 'viewclaimrequests.html', {'claimrequests': claim_requests_with_details})

def update_claimrequest(request):
    try:
        claimrequest_id = request.GET.get("id")
        status = request.GET.get("status")
        if not claimrequest_id or status is None:
            message = "Missing required parameters (id, status)."
        record = get_record_by_id("claimrequests", claimrequest_id)
        if not record:
            message = f"Claim Request with ID {claimrequest_id} not found."
        record["status"] = status
        update_record("claimrequests", claimrequest_id, record)
        claimrequests = get_records("claimrequests")
        return render(request, 'viewclaimrequests.html', {'claimrequests': claimrequests})
    except Exception as e:
        message="An unexpected error occurred."
    records = get_records("claimrequests")
    return render(request, 'viewclaimrequests.html',
                                     {'claimrequests': records, "message": message})
#======================================================================================


