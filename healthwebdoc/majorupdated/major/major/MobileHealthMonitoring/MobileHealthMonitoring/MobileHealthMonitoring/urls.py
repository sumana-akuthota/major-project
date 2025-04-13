"""alumni URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.views.generic import TemplateView

from health import views  # Import the entire 'views' module

urlpatterns = [
    path('admin/', admin.site.urls),

    path('', TemplateView.as_view(template_name='index.html'), name='login'),
    path('login/', TemplateView.as_view(template_name='login.html'), name='login'),
    path('loginaction/', views.login, name='loginaction'),

    path('viewhospitals/', views.view_hospitals, name='viewhospitals'),
    path('updatehospital/', views.update_hospital, name='updatehospitals'),

    path('addhealthpolicy', TemplateView.as_view(template_name='addhealthpolicy.html'), name='addhealthpolicy'),
    path('addhealthpolicyaction', views.add_health_policy, name='addhealthpolicy'),
    path('viewhealthpolicies/', views.view_health_policies, name='view'),
    path('deletehealthpolicy/', views.delete_health_policy, name='delete'),
    path('viewpatients/', views.view_patients, name='view_patients'),
  path('patient_policies_hospitals/', views.get_patient_policies_hospitals, name='patient_policies_hospitals'),
    path('viewclaimrequests/', views.view_claimrequests, name='viewhospitals'),
    path('updateclaimrequest/', views.update_claimrequest, name='updatehospitals'),

    path('viewdetails/', views.view_details, name='viewdetails'),
    path('logout/', views.logout, name='logout'),  # Corrected the name here (was empty)
]