package mutabilityAndImperativeStyle;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.map.*;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.BaseStream;
import java.lang.Iterable;

public class AlphabetFrequencyImperativeStyle 
{
	public static TreeMap<Character, Integer> computeAlphabetFrequency(List<String> collectionOfNames)
	{
		TreeMap<Character, Integer> alphabetTreeMap = new TreeMap<>();
		
		String concatenatedNames = StringUtils.join(collectionOfNames, "")
											  .toLowerCase();
		
		List<Character> alphabetsList = Arrays.asList(ArrayUtils.toObject(concatenatedNames.toCharArray()));

		alphabetsList.forEach(alphabet -> {
				alphabetTreeMap.computeIfAbsent(alphabet, key -> StringUtils.countMatches(concatenatedNames, key.toString() )) ;
			});

		return alphabetTreeMap;
	}	
	
	public static void printAlphabetFrequency(TreeMap<Character, Integer> alphabetTreeMap)
	{	
		alphabetTreeMap.keySet().forEach(key -> System.out.print( key + "=>" + alphabetTreeMap.get(key) + ", "));
	}
	
	public static MultiValueMap<Integer, String> createCollection(List<String> sampleNames)
	{
		MultiValueMap<Integer, String> namesMultiValueMap = new MultiValueMap<>();
		
		sampleNames.forEach(name -> namesMultiValueMap.put(name.length(), name));

		return namesMultiValueMap;
	}
	
	public static void printCollection(MultiValueMap<Integer, String> alphabetsMultiValueMap)
	{
		alphabetsMultiValueMap.keySet().forEach(key -> System.out.print(key + "=>" + alphabetsMultiValueMap.get(key) + ", "));
	}
	
	public static void main(String [] args)
	{
		List<String> samples = Arrays.asList("Adithya","Alex","Alex","Avinash","Benjamin","Brandon","Brian","Chuong","Conno","David","Deepa","Duy","Felipe","Gabriel","Haripriya","James","John","Joshua","Joshua","Narayana","Naveen","Phanindra","Pradhith","Randal","Sachin","Sampath","Tejas","Tharun","Venkat","Viswanathan","Yeshes");				
		
		TreeMap<Character, Integer> alphabetTreeMap = computeAlphabetFrequency(samples);
		printAlphabetFrequency(alphabetTreeMap);
		
		MultiValueMap<Integer, String> namesMultiValueMap = createCollection(samples);
		printCollection(namesMultiValueMap);
				
	}

}