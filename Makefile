all:
	jflex src/LexicalAnalyzer.flex
	javac -d bin -cp src/ src/Main.java
	jar cfe dist/part3.jar Main -C bin .
	javadoc -private src/Main.java -d doc/javadoc

testing_Lex:
	java -jar dist/part3.jar test/Factorial.fs

tested:
	java -jar dist/part3.jar test/testIF.fs

testing:
#	java -jar dist/part3.jar -wt more/testIF.tex test/testIF.fs
	java -jar dist/part3.jar -wt more/trash.tex test/trash.fs
#	java -jar dist/part3.jar -wt more/tree.tex test/euclid.co
#	java -jar dist/part3.jar -wt more/Factorial.tex test/Factorial.fs
#	pdflatex more/tree.tex


clean:
	rm bin/*.class
	rm dist/part3.jar
	rm src/LexicalAnalyzer.java
	rm src/*.java~
	rm doc/javadoc


