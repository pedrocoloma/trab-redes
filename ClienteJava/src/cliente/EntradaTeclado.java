package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EntradaTeclado {
static InputStreamReader isr = new InputStreamReader(System.in);
static BufferedReader br = new BufferedReader(isr);
	
	/*
            Le entrada String do teclado
        */
	public static String leString() throws IOException {
            String x;
            x = br.readLine();
            return x;
	}

	/*
            Le entrada Int do teclado
        */
	public static int leInt() throws IOException {
            String x = leString();
            return Integer.parseInt(x);
	}

	/*
            Le entrada Double do teclado
        */
	public static double leDouble() throws IOException {
            String x = leString();
            return Double.parseDouble(x);
	}
}
