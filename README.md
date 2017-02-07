# Cities-2-Challenge
Graph Challenge
This challenge asks you to write code that reads standard input and writes to standard output

Challenge:
A city is considered 1 degree removed from Chicago if it shares an interstate with Chicago. 
A city that is not directly connected to Chicago but is to a city 1 degree removed is considered 2 degrees removed. And so on.  
Chicago itself is 0 degrees removed, and a city that is not directly or indirectly connected to Chicago has a degree of -1. 
Cities must only appear once, with the lowest degree of connection.
Sort the output by degree descending and then by city and state ascending.

You may assume the following:  
The input file is well-formed: 
each pipe-delimited section will have one or more characters; 
the interstates section will have at least one interstate; 
the population number will be an integer; etc.  
All interstates have the “I-” prefix.  
The same city will not appear more than once in the input file.  
Chicago will be in the input file.

Each line of the output will be in the form:
<Population>|<City>|<State>|<Semicolon-delimited list of interstates that run through this city>

Example Input:
# Skip this line.
6|Seattle|Washington|I-5;I-90
27|Chicago|Illinois|I-94;I-90;I-88;I-57;I-55
10|San Jose|California|I-5;I-80

Each line of the output must be of the form:<Degrees removed from Chicago> <City>, <State>

Example Input:
# Skip this line.
6|Seattle|Washington|I-5;I-90
27|Chicago|Illinois|I-94;I-90;I-88;I-57;I-55
10|San Jose|California|I-5;I-80

Any line that starts wtih # must be skipped

You must write your source code in Solution.java with this definition
public class Solution { 
  public static void main(String[] args) throws Exception {
    //write your code in here
  }
 }
