import firebase_admin
from firebase_admin import credentials, db
import logging
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Firebase setup
try:
    cred = credentials.Certificate(BASE_DIR+"/health/mobilehealthinformation-firebase-adminsdk-fbsvc-f2c61f7561.json")
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://mobilehealthinformation-default-rtdb.firebaseio.com/'
    })
    logging.info("Firebase initialized successfully.")
except Exception as e:
    logging.error(f"Error initializing Firebase: {e}")
    raise

# Create a new record
def create_record(table: str, data: dict) -> bool:
    if not isinstance(data, dict):
        logging.error("Invalid data format. Expected a dictionary.")
        return False
    try:
        db_ref = db.reference(table)
        db_ref.push(data)
        logging.info(f"Record inserted successfully into {table}.")
        return True
    except Exception as e:
        logging.error(f"Error inserting record into {table}: {e}")
        return False

# Read all records from a table
def get_records(table: str):
    try:
        records = db.reference(table).get()
        logging.info(f"Records retrieved successfully from {table}.")
        return records
    except Exception as e:
        logging.error(f"Error retrieving records from {table}: {e}")
        return None

# Update a specific record
def update_record(table: str, record_id: str, record: dict) -> bool:
    if not isinstance(record, dict):
        logging.error("Invalid record format. Expected a dictionary.")
        return False
    try:
        db_ref = db.reference(table).child(record_id)
        db_ref.update(record)
        logging.info(f"Record {record_id} updated successfully in {table}.")
        return True
    except Exception as e:
        logging.error(f"Error updating record {record_id} in {table}: {e}")
        return False

# Delete a specific record
def delete_record(table: str, record_id: str) -> bool:
    try:
        db_ref = db.reference(table).child(record_id)
        db_ref.delete()
        logging.info(f"Record {record_id} deleted successfully from {table}.")
        return True
    except Exception as e:
        logging.error(f"Error deleting record {record_id} from {table}: {e}")
        return False

# Retrieve a specific record by ID
def get_record_by_id(table: str, record_id: str):
    try:
        record = db.reference(table).child(record_id).get()
        logging.info(f"Record {record_id} retrieved successfully from {table}.")
        return record
    except Exception as e:
        logging.error(f"Error retrieving record {record_id} from {table}: {e}")
        return None