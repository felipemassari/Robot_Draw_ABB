/*
 * Programa desenvolvido para a disciplina de Funcamentos de Robotica da Universidade Federal do ABC. O programa lê arquivo de .txt com as 
 * coordenadas em pixel do desenho salvo e converte para linguagem do robô ABB.
 * Primeiramente abra o desenho preto e branco vetorizado (.svg) no Inkscape e salve no formato HPGL. Esse arquivo contem as as coordenadas 
 * dos pixels separados em(PU) para penUp e (PD) para penDown, seguido dos valores dos pontos x,y,z.
 * Posteriomente altere a extensãode HPGL para .txt para o programa fazer a leitura.
 */
package fundamentosRobotica;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat; 

public class Fundamentos {
	static double temp1 = 0;
	static double temp2 = 0;
	static double temp3 = 0;
	static double temp4 = 0;
	
	public static void main(String[] args) {
		
		FileReader fr;
		BufferedReader br;
		StringTokenizer st, stPu, stPd;
		String token;
		int i, tokensNumber, j, countPD;

		try 
		{
			//ler o arquivo .txt no diretorio. Especificar pasta que esta arquivo HPGL
			File arquivo = new File("D:/Workspace_Eclipse/fundamentosRobotica/src/fundamentosRobotica/EradoGelo.txt");
			fr = new FileReader(arquivo);
			br = new BufferedReader(fr);

			while(br.ready())
			{
				st = new StringTokenizer(br.readLine(), ";");	//quebra o .txt a cada ";" que separa PU e PD
				tokensNumber = st.countTokens();
				for(i=0; i<tokensNumber; i++)
				{
					token = st.nextToken();

					//Faz verifiação se o comando é PU ou PD
					if(token.substring(0, 2).compareTo("PU") == 0)		
					{
						stPu = new StringTokenizer(token.substring(2), ",");

						escreve_PU(stPu.nextToken(), stPu.nextToken());
					}
					else if(token.substring(0, 2).compareTo("PD") == 0)
					{
						stPd = new StringTokenizer(token.substring(2), ",");
						countPD = stPd.countTokens();

						//Todas as coordenadas x,y são geradas em sequencia. Loop pega de par (x,y)
						for(j=0; j<countPD/2; j++)
						{
							escreve_PD(stPd.nextToken(), stPd.nextToken());
						}
					}
				}
			}
		}
		catch(IOException e){System.out.println(e.toString());}
	}
	
	static void escreve_PU(String coord1, String coord2){
		try{
			FileWriter fw = new FileWriter("D:/Workspace_Eclipse/fundamentosRobotica/src/fundamentosRobotica/posicao.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);

			int num1 =  Integer.parseInt(coord1);
			int num2 = Integer.parseInt(coord2);
			double number1 = num1/40.0;
			String coordenada1 = precisao(number1);
			double number2 = num2/40.0;
			String coordenada2 = precisao(number2);

			System.out.println("MoveJ Offs (P0, " + coordenada1 + "," + coordenada2 + ", 20), V1000, z0, tool0;");
			bw.write("MoveJ Offs (P0, " + coordenada1 + "," + coordenada2 + ", 20), V1000, z0, tool0;");
			bw.newLine();

			bw.close();
			fw.close();

		}
		catch (IOException ex) {ex.printStackTrace();}
	}
	
	static void escreve_PD(String n1, String n2){
		try{
			FileWriter fw = new FileWriter("D:/Workspace_Eclipse/fundamentosRobotica/src/fundamentosRobotica/posicao.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			int num1 =  Integer.parseInt(n1);
			int num2 = Integer.parseInt(n2);
			double number1 = (num1/40.0);
			double number2 = (num2/40.0);
			
			//criar função resolução
			String [2] coordenadas = resolução(number1, number2);

			System.out.println("MoveL Offs (P0," + coordenadas[1] + "," + coordenadas[2] + ", 0), V1000, z0, tool0;");
				bw.write("MoveL Offs (P0, " + coordenadas[1] + "," + coordenadas[2] + ", 0), V1000, z0, tool0;");
				bw.newLine();
				
			
			bw.close();
			fw.close();
		}
		catch (IOException ex) {ex.printStackTrace();}
	}
	
	//Função ajusta quantas casas decimais tera a coordenada
	static String precisao(double number)
	{
		DecimalFormat df = new DecimalFormat("0.#");
		String numero = df.format(number);
		numero.replace(",", ".");

		return (numero);
	}

	//Função ajusta a distancia entre cada ponto para aumentar ou diminuir a resolução do desenho
	static double resolucao(double number1, double number2)
	{
		if(Math.abs(temp1-number1) > 0.5 || Math.abs(temp2-number2) > 0.5){
				temp1 = number1;
				temp2 = number2;
				temp3 = number1;
				temp4 = number2;
				String numero1 = precisao(number1);
				String numero2 = precisao(number2);

				String [2] coordenadas = [numero1 + number2];
				
			}
		return (coordenadas);
	}
}

