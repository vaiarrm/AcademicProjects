###author: Vaibhav Sharma#Date: 10/22/2016#Description: Calculates the correct value using round function#Output: Returns the correct value##function corrVal = softmax_correctCal(aLast,target)  y = zeros(1,columns(aLast));  a = zeros(size(aLast));  for i=1:columns(aLast)  	l = aLast(:,i:i);  	y(i)=find(l==max(l));  endfor  for i= 1:columns(y)	  a(y(i),i)=1;  endfor  corrVal = sum(all(target==a,1),2);endfunction