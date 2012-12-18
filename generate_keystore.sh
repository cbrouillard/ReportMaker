#!/bin/bash

export CLASSPATH=bcprov-*.jar
CERTSTORE=res/raw/mystore.bks
if [ -a $CERTSTORE ]; then
    rm $CERTSTORE || exit 1
fi
keytool \
      -import \
      -v \
      -trustcacerts \
      -alias 0 \
      -file <(openssl x509 -in mycert.pem) \
      -keystore $CERTSTORE \
      -storetype BKS \
      -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
      -providerpath ./bcprov-jdk16-1.46.jar \
      -storepass ez24get
