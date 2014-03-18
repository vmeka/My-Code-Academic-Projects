def create_charcountmap(list_of_names)
  counts = list_of_names.join
						.downcase
						.split(//)
						.sort
						.group_by{|i| i}.map{|k,v| [k, v.count] }
  Hash[*counts.flatten]
end

def print_charcount(final_charlist) 
  final_charlist.each{|k,v| print "#{k}=>#{v}, "}
end

def create_namegroups (name_list)
  grouped_names =  name_list.group_by{ |s| s.size }
  Hash[grouped_names.sort]
end
                                    
def print_pairs(name_list)
  name_list.each{|k,v| print "#{k}=>[#{v.join(", ")}], "}
end                          
        
                   
samples =%w[Adithya Alex Alex Avinash Benjamin Brandon Brian 
    Chuong Conno David Deepa Duy Felipe Gabriel Haripriya James John Joshua Joshua 
    Narayana Naveen Phanindra Pradhith Randal Sachin Sampath Tejas Tharun Venkat 
    Viswanathan Yeshes]
	
charcount_pairs = create_charcountmap(samples)
print_charcount(charcount_pairs)
    
names_groups = create_namegroups (samples)
print_pairs(names_groups)
	