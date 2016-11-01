package javelindevices.com.javelin_tutorial_1_0;

/**
 * Created by apwan_000 on 10/20/2016.
 */

public class HelloSum {


        public String HelloWorld(){
            System.out.println("Hello World");
            //System.out.println("Total = " + HelloSum.Sum());
            return "Hello World";
        }

        public int Sum(int num1,int num2){
            int total = 0;
            total = num1 + num2;
            return total;
        }

        public static int Sum_1_to_N(int n){
	    int total = 0;

        //For loop. Look it up. Ask question
                              //We use this forloop to calculate the sum from 1 to N
	    for(int i=1; i<=n; i++) {
            total = total + i;
        }
            //Return value. This function returns the value it calculated to the line of code that called it.
            return total;

        }
}