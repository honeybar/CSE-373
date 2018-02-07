
public class test {
	
	public static void main(String[] args) {
		int[] yada = {1, 2, 3, 4};
		arrayTest(yada);
		System.out.println(yada[0]);
		System.out.println(yada[1]);
	}

	public static void arrayTest(int[] bla){
		bla[0] = 4;
		bla[1] = 3;
	}
}
