#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: ./updateLociToolsJar.sh <loci_tools_jar_path>"
    exit 1
fi

jar xf $1 loci/formats/readers.txt
cat loci/formats/readers.txt readers.txt > new_readers.txt
cp new_readers.txt loci/formats/readers.txt
jar uf "$1" loci/formats/readers.txt

echo "$1 is now updated with format readers for iManage."
