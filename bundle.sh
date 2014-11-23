cd maestro-server/target
zip -r ../../maestro.zip AllInOne-1.0.jar lib
cd ../..
zip -r maestro.zip ui
mkdir videos
mkdir configuration
zip -r maestro.zip videos configuration
cd static-files
zip -r ../maestro.zip *