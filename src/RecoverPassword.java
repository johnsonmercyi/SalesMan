import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RecoverPassword {
	
	public RecoverPassword () {
		super();
	}
	
	private int smallestNumber (int num[]) {
		int smallest = num[0];
		for (int i = 0; i < num.length; i++) {
			
			if (num[i] >= smallest)
				continue;
			
			smallest = num[i];
			
		}
		
		return smallest;
	}
	
	public static void main(String[] args) {
		
		RecoverPassword rp = new RecoverPassword();
		
		
		int p1[] = {
				10,9,0,11,1,9,7,90,87,54
		}, p2[] = {
				10,9,7,11,0,9,7,57,90,46
		}, p3[] = {
				10,11
		};
		
		int pLowest = rp.smallestNumber(p3), pHighest;
		
		Set <Integer> allItems = new TreeSet<Integer>();
		List <int[]> passwordArrays = new ArrayList<int[]>();
		passwordArrays.add(p1);
		passwordArrays.add(p2);
		passwordArrays.add(p3);
		
		Iterator<int []> pit = passwordArrays.iterator();
		while (pit.hasNext()) {
			
			int item [] = pit.next();
			
			for (int i = 0; i < item.length; i++) {
				allItems.add(item[i]);
			}
		}
		
		System.out.println(pLowest);
		
		System.out.println(allItems.toString());
		
	}
	
}
