package com.suryoday.payment.payment;

import java.text.DecimalFormat;
import java.util.Scanner;

public class EmiCaluator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//double d=5000*(1-(1+2.33/100.00)^(-24))/2.33/100;
	//	System.out.println("final output"+d);
		
		
		Scanner a = new Scanner(System.in);
        
        double principal, rate, time, emi;
  
        System.out.print("Enter EMI: ");
        principal = a.nextFloat();
      
        System.out.print("Enter rate: ");
        rate = a.nextFloat();
      
        System.out.print("Enter time in year: ");
        time = a.nextFloat();
      
        rate=rate/(12*100); 
        time=time*12; 
        
        System.out.println("rate"+rate+""+"time :"+time);
      
      
        emi= (principal*rate*Math.pow(1+rate,time))/(Math.pow(1+rate,time)-1);
        
        double result = principal * (1 - Math.pow(1 + rate, -time)) / rate;
        
        DecimalFormat df = new DecimalFormat("#.##");

        // Format the double value using the DecimalFormat object
        String formattedValue = df.format(result);
        System.out.println("Result: " + formattedValue);
        
        
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the principle amount");
        double p=sc.nextDouble();
        System.out.println("Enter the rate of intrest for annum");
        double r=sc.nextDouble();
        r=r/(12*100);
        System.out.print("Enter time in year: ");
        
        int n=sc.nextInt();
        double time1=n*12; 
        double E=p*r*(Math.pow(1+r,time1)/(Math.pow(1+r,time1)-1));
        System.out.print("The EMI is ");
        System.out.printf("%.2f",E);
        
      
      // System.out.print("Best Offer  is= "+d+"\n");
	}

}
