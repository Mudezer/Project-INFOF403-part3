all:
	jflex src/LexicalAnalyzer.flex
	javac -d bin -cp src/ src/Main.java
	jar cfe dist/part3.jar Main -C bin .
	javadoc -private src/Main.java -d doc/javadoc

testing_Lex:
	java -jar dist/part3.jar test/Factorial.fs

tested:
	java -jar dist/part3.jar test/trash.fs

testing:
#	java -jar dist/part3.jar test/testIF.fs
#	java -jar dist/part3.jar test/trash.fs
#	java -jar dist/part3.jar test/euclid.co
	java -jar dist/part3.jar test/Factorial.fs
#	pdflatex more/tree.tex


clean:
	rm bin/*.class
	rm dist/part3.jar
	rm src/LexicalAnalyzer.java
	rm src/*.java~
	rm doc/javadoc


