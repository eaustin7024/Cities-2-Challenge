import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Comparator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * @author: E.Austin
 * For this solution, the internet tool doesn't accept packages and requires one Solution file since we
 * can't break the different classes into packages, I'll use inner classes to faciliate this need.
 */
 public class Solution {

	private String startingVertexCityName;
	private List<CityByDegrees> cityByDegreesList;
	private final static int MINUS_ONE_DEGREE = -1;
	private final static int ZERO_DEGREE = 0;
	private CityByDegrees startingVertexCity;
	/**
	 * Main Program for Degrees from City
	 *
	 */
	public static void main(String[] args) throws Exception {

			/*
			In future add a parameter to accept default city from commandline
			For now hardcode the default city as Chicago.
			*/
			//Set the default/vertex city to run the report against
			Solution solution = new Solution();
			solution.setStartingVertexCityName("Chicago");

			//Read in the input from STDIN and parse it
			solution.setCityByDegreesList(solution.parseCityInput());

			//Set the adjacent cities for each city in the list
			solution.setAdjacentCities(solution.getCityByDegreesList());

			//determine the degrees to the vertex city for each city
			solution.setDegreesForCities();

			//Sort the list of cities by degrees descending then city ascending then state ascending
			Collections.sort(solution.getCityByDegreesList(), solution.new CityByDegreesChainedComparator(
			                solution.new DegreeComparator(),
			                solution.new CityComparator(),
			                solution.new StateComparator())
			);

			//print the result to the command line
			solution.printResults(solution.getCityByDegreesList());

	}

	/**
	 * Set the vertix city name to start from
	 * @param vertexCity
	 */
	private void setStartingVertexCityName(String vertexCityName) {
		this.startingVertexCityName = vertexCityName;
	}

	/**
	 * Return the vertex city name we want to start from
	 * @return java.lang.String
	 */
	private String getStartingVertexCityName() {
		return this.startingVertexCityName;
	}

    /**
     * Return the vertex city we want to start from
     * @param return CityByDegrees
     */
	private CityByDegrees getStartingVertexCity() {
		return this.startingVertexCity;
	}

    /**
     * Set the vertex city to start from
     * @param vertexCity
     */
	private void setStartingVertexCity(CityByDegrees vertexCity) {
		this.startingVertexCity = vertexCity;
	}

	/**
	 * Set the city by degrees list
	 * @param cityDegreesList
	 */
	private void setCityByDegreesList(List<CityByDegrees> cityDegreesList) {
		this.cityByDegreesList = cityDegreesList;
	}

	/**
	 * Return the city by degrees list
	 * @return CityByDegrees
	 */
	private List<CityByDegrees> getCityByDegreesList() {
		return this.cityByDegreesList;
	}

    /**
     * Parse the input form STDIN and return a list of cities ready for processing
     * @return List
     */
    private List<CityByDegrees> parseCityInput() throws IOException,NumberFormatException {

		    BufferedReader br = null;

	        try
	        {
	            	List <CityByDegrees> cityList = new ArrayList<CityByDegrees>();

                    // first try to read from standard input
	                br = new BufferedReader(new InputStreamReader(System.in));

	                String inputLine  = null;

	                //Retrieve and store the different tokens from each line from STDIN
					while ((inputLine = br.readLine()) != null)
					{
						inputLine = inputLine.trim();
						if(inputLine.startsWith("#")) continue;
						String[] tokens = inputLine.split("\\|");
						String[] interstates = tokens[3].split(";");
						List interstateList = Arrays.asList(interstates);

						CityByDegrees cityByDegrees = new CityByDegrees(Integer.parseInt(tokens[0].trim()), tokens[1].trim(), tokens[2].trim(), interstateList);
						if(cityByDegrees.getCity().equalsIgnoreCase(getStartingVertexCityName())) {
							cityByDegrees.setDegrees(ZERO_DEGREE);
							setStartingVertexCity(cityByDegrees);
						}
						cityList.add(cityByDegrees);
					}//end while

					return cityList;
	        }
			finally {
						try {
							if (br != null) br.close();

						} catch (IOException ex) {

							ex.printStackTrace();
						}
			}
	}

    /**
     * Print out the list of city by degrees to the command line
     * @param cities
     */
    private void printResults(List<CityByDegrees> cities){
		StringBuffer buffer = new StringBuffer();
		for(int i =  0; i < cities.size(); i++) {
			CityByDegrees tempCity =  cities.get(i);
			buffer.append(tempCity.getDegrees()).append(" ").append(tempCity.getCity()).append(", ").append(tempCity.getState());
			System.out.println(buffer.toString());
			buffer.setLength(0);
		}
	}

    /**
     * Sets the adjacent cities for each city in the passed in list
     * @param cityList
     */
	private void setAdjacentCities(List <CityByDegrees> cityList)
	{
		//get a mapping of interstates to cities
		HashMap<String, Set<CityByDegrees>> interstateMap = new HashMap<String, Set<CityByDegrees>>();
		for(int i =  0; i < cityList.size(); i++)
		{
			List interstates = ((CityByDegrees)cityList.get(i)).getInterstates();
			for(int j = 0; j < interstates.size(); j++)
			{
				String interstate = (String)interstates.get(j);
				if(interstateMap.containsKey(interstate)) {
					((Set)interstateMap.get(interstate)).add(cityList.get(i));
				}
				else{
					Set <CityByDegrees> interstateCitySet = new HashSet<CityByDegrees>();
					interstateCitySet.add(cityList.get(i));
					interstateMap.put(interstate, interstateCitySet);
				}
			}
		}//end for

		//get a mapping of vertices to each city
		for(int i =  0; i < cityList.size(); i++)
		{
			List interstates = ((CityByDegrees)cityList.get(i)).getInterstates();
			for(int j = 0; j < interstates.size(); j++)
			{

				String interstate = (String)interstates.get(j);
				HashSet<CityByDegrees> interstateCitySet = (HashSet)interstateMap.get(interstate);

				if(interstateCitySet.contains(cityList.get(i))) {
					Iterator iterator = interstateCitySet.iterator();
					while(iterator.hasNext()) {
						CityByDegrees temp = (CityByDegrees)iterator.next();
						if(!temp.getCity().equals( ((CityByDegrees)cityList.get(i)).getCity()))
						{
							((CityByDegrees)cityList.get(i)).addAdjacentCity(temp);
						}

					}
				}

			}
		}//end for

	}

    /**
     * Set the degrees for each city to the vertex city
     */
	private void setDegreesForCities() {

		//Retrieve the staring vertex city
		LinkedList<CityByDegrees> linkedList = new LinkedList<CityByDegrees>();
		getStartingVertexCity().setVisited(true);
		linkedList.add(getStartingVertexCity());

		//Go through and determine the degrees to the vertext city for each city
		while(linkedList.size() > 0) {
			CityByDegrees cityByDegrees = (CityByDegrees)linkedList.remove();
			HashSet <CityByDegrees>adjacentCities = cityByDegrees.getAdjacentCities();
			Iterator iterator = adjacentCities.iterator();

			while(iterator.hasNext()) {
				CityByDegrees currentCity = (CityByDegrees)iterator.next();
				if(!currentCity.isVisited()) {
					currentCity.setVisited(true);
					if(cityByDegrees.getDegrees() >= 0) {
						currentCity.setDegrees(cityByDegrees.getDegrees() + 1);
					}
					linkedList.add(currentCity);
				}
			}//end inner while
		}//end outer while
	}

	/**
	 * Class to hold the data structure for the cities
	 */
    class CityByDegrees {
		private int population;
		private String city;
		private String state;
		private List <String> interstates;
		private int degrees;
		private boolean visited;
		private HashSet<CityByDegrees> adjacentCitySet;
		private boolean startingVertexCity;

        /**
         * Default constructor
         * @param population
         * @param city
         * @param state
         * @param interstates
         */
		public CityByDegrees(int poputlation, String city, String state, List interstates)
		{
			this.population = population;
			this.city = city;
			this.state = state;
			this.interstates = interstates;
			this.degrees = MINUS_ONE_DEGREE;
			this.adjacentCitySet = new HashSet<CityByDegrees>();
		}

		/**
		 * Return the population
		 * @return int
		 */
		public int getPopulation() {
			return this.population;
		}

		/**
		 * Return the city
		 * @return String
		 */
		public String getCity() {
			return this.city;
		}

        /**
         * Return the state
         * @return String
         */
		public String getState() {
			return this.state;
		}

        /**
         * Return the list of interstate
         * @return java.util.List
         */
		public List getInterstates() {
			return this.interstates;
		}

        /**
         * Return the adjacent cities
         * @return java.util.HashSet
         */
		public HashSet<CityByDegrees> getAdjacentCities() {
			return this.adjacentCitySet;
		}

        /**
         * Return the degrees
         * @return int
         */
		public int getDegrees() {
			return this.degrees;
		}

		/**
		 * Set the degrees
		 * @param degrees
		 */
		public void setDegrees(int degrees) {
			this.degrees = degrees;
		}

		/**
		 * Set the population
		 * @param population
		 */
		public void setPoputlation(int population) {
			this.population = population;
		}

		/**
		 * Set the city
		 * @param city
		 */
		public void setCity(String city) {
			this.city = city;
		}

		/**
		 * Set the state
		 * @param state
		 */
		public void setState(String state) {
			this.state = state;
		}

        /**
         * Set the visited flag
         * @param visited
         */
		public void setVisited(boolean visited) {
			this.visited = visited;
		}

        /**
         * Return the visited flag
         * @return boolean
         */
	    public boolean isVisited() {
			return this.visited;
		}

		/**
		 * Returns true if this city is the starting vertex
		 * @return boolean
		 */
		public boolean isStartingVertexCity() {
			return (getDegrees() == ZERO_DEGREE);
		}

		/**
		 * Add the adjacent city to the set of adjacent cities
		 * @param adjCity
		 */
		protected void addAdjacentCity(CityByDegrees adjCity) {
			adjacentCitySet.add(adjCity);
		}
	}

    /**
     * Class to sort the cities by degrees descending
     */
	class DegreeComparator implements Comparator<CityByDegrees> {
	    @Override
	    public int compare(CityByDegrees city1, CityByDegrees city2) {
	        if(city1.getDegrees() < city2.getDegrees()) {
				return 1;
			}
			else if(city1.getDegrees() > city2.getDegrees()) {
				return -1;
			}
			else return 0;
	    }
	}

    /**
     * Class to sort the cities by city ascending
     */
	class CityComparator implements Comparator<CityByDegrees> {
	    @Override
	    public int compare(CityByDegrees city1, CityByDegrees city2) {
			return city1.getCity().compareTo(city2.getCity());
	    }
	}

	/**
	 * Class to sort the cities by state ascending
	 */
	class StateComparator implements Comparator<CityByDegrees> {
	    @Override
	    public int compare(CityByDegrees city1, CityByDegrees city2) {
			return city1.getState().compareTo(city2.getState());
	    }
	}

    /**
     * Class to chain the different comparators
     */
	class CityByDegreesChainedComparator implements Comparator<CityByDegrees> {

	    private List<Comparator<CityByDegrees>> listComparators;

	    @SafeVarargs
	    public CityByDegreesChainedComparator(Comparator<CityByDegrees>... comparators) {
	        this.listComparators = Arrays.asList(comparators);
	    }

	    @Override
	    public int compare(CityByDegrees city1, CityByDegrees city2) {
	        for (Comparator<CityByDegrees> comparator : listComparators) {
	            int result = comparator.compare(city1, city2);
	            if (result != 0) {
	                return result;
	            }
	        }
	        return 0;
	    }
	}

 }
