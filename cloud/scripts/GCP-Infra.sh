#!/bin/bash

# Set your GCP project ID and other variables
PROJECT_ID="sustained-digit-399111"
CLUSTER_NAME="cluster-gcp-deployment"
REGION="us-central1-c"
TOPIC_NAME="Vehicle"
DB_REGION="us-east1"
DB_TYPE="firestore-native"

# Enable required APIs
gcloud services enable container.googleapis.com pubsub.googleapis.com cloudfunctions.googleapis.com firestore.googleapis.com cloudbuild.googleapis.com eventarc.googleapis.com run.googleapis.com artifactregistry.googleapis.com
# Create a GKE cluster
gcloud container clusters create $CLUSTER_NAME --num-nodes=2 --region=$REGION --scopes "https://www.googleapis.com/auth/cloud-platform"
gcloud container clusters get-credentials $CLUSTER_NAME --zone $REGION --project $PROJECT_ID
# Create a Pub/Sub topic
gcloud pubsub topics create $TOPIC_NAME --project=$PROJECT_ID
#Create Firestore DB
gcloud alpha firestore databases create --location=$DB_REGION --type=$DB_TYPE

echo "Infrastructure and services set up successfully."
