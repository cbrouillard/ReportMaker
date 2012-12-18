#!/bin/bash

echo | openssl s_client -connect $1 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > mycert.pem
