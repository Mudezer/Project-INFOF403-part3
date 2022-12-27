BEGINFactorial%% Compute the factorial of a number.
If the input number is negative, print -1. %%
READ(number),:: Read a number from user input
result:=1,IF(number>-1)THENWHILEnumber>0DOresult:=result*number,
number:=number-1,:: decrease number
END,ELSE:: The input number is negative
result:=-1,END,PRINT(result),IFabbbENDENDDOEND

