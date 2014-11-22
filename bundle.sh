mkdir output

cd maestro-server/target
zip -r ../../output/maestro-$1.zip AllInOne-1.0.jar lib
cd ../.. 
cd ui/omny-angular
zip -r ../../output/maestro-$1.zip public_html