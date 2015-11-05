JAVA_HOME="$HOME/rep/pkg/tools/jdk/1.6.0_21"
PATH=$JAVA_HOME/bin:$PATH
java -DInputFile=$InputFile -cp lib/client-jar.jar:lib/ij.jar:. FindCenterTest
