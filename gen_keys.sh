#!/bin/bash

# author: Professores de Sistemas Distribuídos - Instituto Superior Técnico
################################################################################
# Script to generate signed X509 certificates
# usage:
#     ./gen_keys.sh <server_name_1 server_name_2 server_name_3...>
# More information regarding signed certificates consult:
# - https://docs.oracle.com/cd/E19509-01/820-3503/ggeyj/index.html
# - https://docs.oracle.com/cd/E19509-01/820-3503/ggezy/index.html
################################################################################

#constants
NOW=$(date +"%Y_%m_%d__%H_%M_%S")
CA_ALIAS="ca"
STORE_PASS="ins3cur3"
KEY_PASS="1nsecure"
CA_CERTIFICATE_PASS="1ns3cur3"
CA_CSR_FILE="ca.csr"
D_NAME="CN=DistributedSystems,OU=DEI,O=IST,L=Lisbon,S=Lisbon,C=PT"
SUBJ="/CN=DistributedSystems/OU=DEI/O=IST/L=Lisbon/C=PT"
KEYS_VALIDITY=90
OUTPUT_FOLDER="keys"
CA_FOLDER="$OUTPUT_FOLDER/ca"
STORE_FILE="$CA_FOLDER/ca-keystore.jks"
CA_PEM_FILE="$CA_FOLDER/ca-certificate.pem.txt"
CA_KEY_FILE="$CA_FOLDER/ca-key.pem.txt"

ENTITIES=('UpaBroker' 'UpaTransporter1');

if [ "$#" -eq 0 ]; then
	echo ${ENTITIES[@]}
else
	echo $1
	for i in `seq 2 $1`
	do
		ENTITIES=("${ENTITIES[@]}" "UpaTransporter$i");
	done
	echo ${ENTITIES[@]}
fi
################################################################################
# 1 - First the CA Certificate is generated
# This certificate is used to sign other certificates
# This procedure is done once for the CA and the generated files (*.pem.txt)
# are used to sign the certificates of the other entities
################################################################################
echo "Checking if keys folder exists"

if [ -d "$OUTPUT_FOLDER" ]; then
	echo "Old folder found! It will be deleted."
  	rm -rf $OUTPUT_FOLDER
else 
	echo "Folder does not exist and will be created"
fi

mkdir $OUTPUT_FOLDER
echo "$OUTPUT_FOLDER has been created, now we will generate certificates"
mkdir $CA_FOLDER
echo "Generating the CA certificate..."
openssl req -new -x509 -keyout $CA_KEY_FILE -out $CA_PEM_FILE -days $KEYS_VALIDITY -passout pass:$CA_CERTIFICATE_PASS -subj $SUBJ
echo "CA Certificate generated."
echo "Importing the CA certificate to the keystore of $CA_ALIAS..."
keytool -import -keystore $STORE_FILE -file $CA_PEM_FILE  -alias $CA_ALIAS -keypass $KEY_PASS -storepass $STORE_PASS -dname $D_NAME -noprompt 
################################################################################
# 2 - Then, for each entity (given as an argument) the certificates argument
# generated, signed and imported into the entities keystore
################################################################################

for server_name in ${ENTITIES[@]}
do
  D_NAME="CN=$server_name,OU=DEI,O=IST,L=Lisbon,S=Lisbon,C=PT"
  server_folder=$OUTPUT_FOLDER/$server_name
  mkdir $server_folder
  server_kerystore_file="$server_folder/$server_name.jks"
  csr_file="$server_folder/$server_name.csr"
  echo "Generating keypair of $server_name..."
  keytool -keystore $server_kerystore_file -genkey -alias $server_name -keyalg RSA -keysize 2048 -keypass $KEY_PASS -validity $KEYS_VALIDITY -storepass $STORE_PASS  -dname $D_NAME
  echo "Generating the Certificate Signing Request of $server_name..."
  keytool -keystore $server_kerystore_file -certreq -alias $server_name -keyalg rsa -file $csr_file -storepass $STORE_PASS -keypass $KEY_PASS
  echo "Generating the signed certificate of $server_name..."
  openssl  x509  -req  -CA $CA_PEM_FILE -CAkey $CA_KEY_FILE -passin pass:$CA_CERTIFICATE_PASS -in $csr_file -out "$server_folder/$server_name.cer" -days $KEYS_VALIDITY -CAcreateserial
  echo "Importing the CA certificate to the keystore of $server_name..."
  keytool -import -keystore $server_kerystore_file -file $CA_PEM_FILE  -alias $CA_ALIAS -keypass $KEY_PASS -storepass $STORE_PASS -noprompt
  echo "Importing the signed certificate of $server_name to its keystore"
  keytool -import -keystore $server_kerystore_file -file "$server_folder/$server_name.cer" -alias $server_name -storepass $STORE_PASS -keypass $KEY_PASS
  echo "Importing the signed certificate of $server_name to the $CA_ALIAS keystore"
  keytool -import -keystore $STORE_FILE -file "$server_folder/$server_name.cer" -alias $server_name -storepass $STORE_PASS -keypass $KEY_PASS -noprompt
  echo "Removing the Certificate Signing Request (.csr file)..."
  rm "$server_folder/$server_name.csr"
done

