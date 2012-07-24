#!/bin/bash
FILES=/usr/u/doc/bkaushik/101/p31/*
for f in $FILES
do 
echo $f
cd $f
cp feedback.txt feedback.txt.0
rm feedback.txt
cd ..
done


