import java.io.*;
import java.util.*;
class Methods
{	
	static int NumOfBlock=0,ReturnValInt=Integer.MAX_VALUE,lineM=0,lineMain=-1;
	static int [] ReturnValArr;
	static String NowInMethod = "";
	static boolean visScanner = false;
	static LinkedList<String> lines;
	static int[][] PrevBlock;  
	static LinkedList<String >[][] varName;
	static LinkedList<Integer>[][] varVal;
	static LinkedList<String>[][] arrName;
	static LinkedList<Integer>[][][] Arrays;
	static LinkedList<Integer>[] lineFuncBlock;
	static LinkedList<String> FuncKind = new LinkedList<String>();
	static LinkedList<Integer> FuncLine = new LinkedList<Integer>();
	static LinkedList<String> FuncName = new LinkedList<String>();
	static boolean [][] IfElse;
	static File file;
	static Scanner Read;
	static Scanner scan;
	public static void MethodRead1() throws FileNotFoundException
	{
		file = new File("myData.java");
		Read = new Scanner(file);
		
		lines = new LinkedList<String>();
		while(Read.hasNextLine())
		{
			String line = Read.nextLine().trim();
			if(line!="")
				lines.add(line);
		}
		for(int line=0 ; line<lines.size() ; line++)//declare NumOfBlock
		{
			for(int i=0 ; i<lines.get(line).length() ; i++)
				if(lines.get(line).charAt(i)=='{')
					NumOfBlock++;
		}
	}
	public static void MethodRead2()
	{
		checkMethodsMain();
		
		varName =  new LinkedList[FuncName.size()][NumOfBlock];
		varVal =  new LinkedList[FuncName.size()][NumOfBlock];
		arrName = new LinkedList[FuncName.size()][NumOfBlock];
		Arrays = new LinkedList[FuncName.size()][NumOfBlock][100];
		lineFuncBlock = new LinkedList[FuncName.size()];
		PrevBlock = new int[FuncName.size()][NumOfBlock];
		IfElse = new boolean [FuncName.size()][100];
		
		for(int j=0 ; j<FuncName.size() ; j++)
			for(int i=0 ; i<NumOfBlock ; i++)
			{
				varName[j][i]=new LinkedList<String>();
				varVal[j][i]=new LinkedList<Integer>();
				arrName[j][i] = new LinkedList<String>();
			}
		for(int i=0 ; i<FuncName.size() ; i++)
		{
			lineFuncBlock[i] = new LinkedList<Integer>();
		}
		for(int k=0 ; k<FuncName.size() ; k++)
			for(int i=0 ; i<NumOfBlock ; i++)
				for(int j=0 ; j<100 ; j++)
					Arrays[k][i][j] = new LinkedList<Integer>();
	}
	public static void checkSyntax()
	{
		if(!lines.get(0).equals("import java.util.Scanner;"))
			System.out.println("error in line "+0+" import Scanner Package.");
		
		if(!lines.get(1).startsWith("public class"))
			System.out.println("error in line : "+1+" your methods & classes must be public.");
		
		for(int l=0 ; l<lines.size() ; l++)
		{
			if(lines.get(l).startsWith("while") || lines.get(l).startsWith("if") || lines.get(l).startsWith("}") || lines.get(l).startsWith("{") || lines.get(l).startsWith("public") || lines.get(l).startsWith("else") || lines.get(l).startsWith(""))
				continue;
			else if(!lines.get(l).endsWith(";"))
				System.out.println("error in containing simicolon.");
		}
	}
	public static void Print(String printline)
	{
		StringTokenizer str = new StringTokenizer(printline,"()");
		String [] linePrint = new String[str.countTokens()];
		
		for(int i=0 ; i<linePrint.length ; i++)
			linePrint[i]=str.nextToken().trim();
		
		boolean ok=true;
		if(!linePrint[linePrint.length-1].equals(";"))
		{
			System.out.println("error in';' in line:"+lineM);
			ok=false;
		}
		if(ok)
		{
			if(linePrint[1].startsWith("\"") && linePrint[1].endsWith("\""))
				System.out.println(linePrint[1].substring(1,linePrint[1].length()-1));
			
			String Actual = "";
			String LastMethod = NowInMethod;
			for(int i=0 ; i<FuncName.size() ; i++)
				if(linePrint[1].startsWith(FuncName.get(i)) && FuncKind.get(i).equals("int"))//Function
				{
					Actual+=linePrint[1];Actual+="(";
					for(int x=2 ; x<linePrint.length-1 ; x++)
						Actual+=linePrint[x];
					Actual+=")";
					int LineRes = lineM;
					checkGiveVariFunc(lines.get(FuncLine.get(i)),Actual,FuncName.get(i),LastMethod);
					System.out.println(FuncReturnInt(FindBracketBlock(FuncLine.get(i),lines)));
					lineM = LineRes;
					NowInMethod = LastMethod;
					break;
				}
			if(Operators(linePrint[1])!=Integer.MAX_VALUE)
				System.out.println(Operators(linePrint[1]));
		}
	}
	public static void GiveValue(String str)
	{
		scan = new Scanner(System.in);
		if(str.length()==0 || str.startsWith("int[]") || str.startsWith("int []") || str.startsWith("[]"))
			return;
		
		StringTokenizer Value = new StringTokenizer(str,",;= ");
		String [] Val = new String[Value.countTokens()];
		
		for(int i=0 ; i<Val.length ; i++)
			Val[i]=Value.nextToken().trim();
		
		int nowInBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM);
		
		for(int Befor=0 ; nowInBlock-Befor>=0 ; Befor++)
		{
			if(varName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].contains(Val[0]) && Operators(Val[1])!=Integer.MAX_VALUE)			varVal[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].set(varName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].indexOf(Val[0]), Operators(Val[1]));
		
			else if(varName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].contains(Val[0]) && Val[1].contains("nextInt()"))
			{
				if(!visScanner)
					System.out.println("error in definning Scanner.");
				varVal[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].set(varName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].indexOf(Val[0]),scan.nextInt());
			}
		}
	}
	public static boolean beName(String str)
	{
		if(BeInt(str.charAt(0)+""))
			return false;
		
		String Alph = "abcdefghijklmnopqrstuvwxyz1234567890";
		for(int j=1 ; j<str.length() ; j++)
		{
			boolean ok=false;
			for(int i=0 ; i<Alph.length() ; i++)
				if(str.charAt(j)==Alph.charAt(i))
					ok=true;
		if(!ok)
			return false;
		}
		return true;
	}
	public static void checkIF(LinkedList<String> Blocklines)
	{
		String Sign = "";
		for(int i=0 ; i<Blocklines.get(lineM).length() ; i++)
		{
			if(Blocklines.get(lineM).substring(i,i+2).equals(">=") || Blocklines.get(lineM).substring(i,i+2).equals("<=") || 
					Blocklines.get(lineM).substring(i,i+2).equals("!=") || Blocklines.get(lineM).substring(i,i+2).equals("=="))
			{
				Sign = Blocklines.get(lineM).substring(i,i+2);
				break;
			}
			if(Blocklines.get(lineM).charAt(i)=='<' || Blocklines.get(lineM).charAt(i)=='>')
			{
				Sign = Blocklines.get(lineM).charAt(i)+"";
				break;
			}
		}
		StringTokenizer stIF = new StringTokenizer(Blocklines.get(lineM),"()<=>!" );
		String[] arrIF=new String[3];
		boolean ok = true;
		
		for(int x=0 ; x<arrIF.length ; x++)
			arrIF[x]=stIF.nextToken();
		
		for(int i=1 ; i<arrIF.length ; i++)
			if(Operators(arrIF[i])==Integer.MAX_VALUE)
			{
				System.out.println("error in declaring variables.");
				ok = false;
			}
		if(ok)
		{
				if(Sign.equals(">"))
				{
					if(Operators(arrIF[1]) > Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
				else if(Sign.equals("<"))
				{
					if(Operators(arrIF[1]) < Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
				else if(Sign.equals("=="))
				{
					if(Operators(arrIF[1]) == Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
				else if(Sign.equals("!="))
				{
					if(Operators(arrIF[1]) != Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
				else if(Sign.equals(">="))
				{
					if(Operators(arrIF[1]) >= Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
				else if(Sign.equals("<="))
				{
					if(Operators(arrIF[1]) <= Operators(arrIF[2]))
						IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
					else
						lineM += FindBracketBlock(lineM,Blocklines).size()-1;
				}
			}
		}
	public static LinkedList<String> FindBracketBlock(int line,LinkedList<String> lines)
	{
		int P=0,EndLine=0;
		boolean vis=false;
		LinkedList<String> Block = new LinkedList<String>();
		for(int j=line ; j<lines.size() ; j++)
		{
			for(int i=0 ; i<lines.get(j).length() ; i++)
			{
				if(lines.get(j).charAt(i)=='{')
					P++;
				if(lines.get(j).charAt(i)=='}')
					P--;
				if(P==0 && vis)
				{
					EndLine=j;
					break;
				}
				if(P==1)
					vis=true;
			}
			if(P==0 && vis)
				break;
		}
		for(int i=line ; i<=EndLine ; i++)
			Block.add(lines.get(i));
		return Block;
	}
	public static void FuncAddKindNameLine(int line)
	{
			String str = lines.get(line);
			StringTokenizer Tokens = new StringTokenizer(str," ()");
			String [] LineToken = new String[Tokens.countTokens()];
			
			for(int i=0 ; i<LineToken.length ; i++)
				LineToken[i] = Tokens.nextToken();
			
			if(LineToken[3].equals("[]"))
			{
				FuncKind.add("int[]");
				FuncName.add(LineToken[4]);
				FuncLine.add(line);
			}
			else
			{
				FuncKind.add(LineToken[2]);
				FuncName.add(LineToken[3]);
				FuncLine.add(line);
			}
		}
	public static void checkGiveVariFunc(String FormalPar,String ActualPar,String nowInMethod,String LastMethod)
	{
		if(FormalPar.length()==0 || ActualPar.length()==0)
			return;
		int nowInBlock = lineFuncBlock[FuncName.indexOf(nowInMethod)].get(1);
		StringTokenizer TokenFormalPar = new StringTokenizer(FormalPar ,"(),;");
		StringTokenizer TokenActualPar = new StringTokenizer(ActualPar ,"(),;");
		String [] TFP = new String[TokenFormalPar.countTokens()];
		String [] TAP = new String[TokenActualPar.countTokens()];
		
			for(int i=0 ; i<TFP.length ; i++)
				TFP[i]=TokenFormalPar.nextToken();
			for(int i=0 ; i<TAP.length ; i++)
				TAP[i]=TokenActualPar.nextToken();
			for(int l=0 ; l<TFP.length ; l++)//check in front of ( ) of function
			{
				if(TFP[l].startsWith("int[]") || TFP[l].startsWith("int []"))
				{
					arrName[FuncName.indexOf(nowInMethod)][nowInBlock].add(TFP[l].substring(6).trim());
					for(int i=0 ; i<Arrays[FuncName.indexOf(LastMethod)][nowInBlock][arrName[FuncName.indexOf(LastMethod)][nowInBlock].indexOf(TAP[l])].size() ; i++)
						Arrays[FuncName.indexOf(nowInMethod)][nowInBlock][arrName[FuncName.indexOf(nowInMethod)][nowInBlock]
								.indexOf(TFP[l].substring(6).trim())].add(Arrays[FuncName.indexOf(LastMethod)][nowInBlock]
										[arrName[FuncName.indexOf(LastMethod)][nowInBlock].indexOf(TAP[l])].get(i));
				}
				else if(TFP[l].startsWith("int") && Operators(TAP[l])!=Integer.MAX_VALUE)
				{
					varName[FuncName.indexOf(nowInMethod)][nowInBlock].add(TFP[l].substring(4));
					varVal[FuncName.indexOf(nowInMethod)][nowInBlock].add(Operators(TAP[l]));
				}
			}
		}
	public static int FuncReturnInt(LinkedList<String> BlockFunc) 
	{
		boolean vis=false;
		for(lineM=0; lineM<BlockFunc.size() ; lineM++)
		{
			checkInBlock(BlockFunc);
			
			if(ReturnValInt!=Integer.MAX_VALUE)
				return ReturnValInt;
			
			else if(BlockFunc.get(lineM).equals("return"))
			{
				vis=true;
				ReturnInt(BlockFunc,lineM);
			}
		}
		if(!vis)
			System.out.println("Function must return int.");
		return -1;
	}
	public static int[] ReturnArr(LinkedList<String> BlockFunc)
	{
		int nowInBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM);
		String str = BlockFunc.get(lineM).substring(7,BlockFunc.get(lineM).length()-1);
		
		if(arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(str)>=0)
		for(int i=0 ; i<ReturnValArr.length ; i++)
			ReturnValArr[i] = Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(str)].get(i);
		
		if(arrName[FuncName.indexOf(NowInMethod)][nowInBlock].contains(str))
				return ReturnValArr;
		return null;
	}
	public static void FuncReturnArr(LinkedList<String> BlockFunc) 
	{
		boolean vis=false;
		for(lineM=0 ; lineM<BlockFunc.size() ; lineM++)
		{
			checkInBlock(BlockFunc);
			
			if(BlockFunc.get(lineM).equals("return"))
			{
				vis=true;
				ReturnValArr = ReturnArr(BlockFunc);
			}
		}
		if(!vis && ReturnValArr.length==0)
			System.out.println("Function must return Array.");
	}
	public static void ReadArray(String str,int nowInBlock)
	{
		scan = new Scanner(System.in);
		
		StringTokenizer Arr = new StringTokenizer(str," ;[]="); 
		String [] arr = new String[Arr.countTokens()];
		
		for(int x=0 ; x<arr.length ; x++)
			arr[x]=Arr.nextToken().trim();
		
		int idxEq = -1;
		for(int i=0 ; i<str.length() ; i++)
			if(str.charAt(i)=='=')
				idxEq = i;
		
		if(idxEq!=-1 && Operators(arr[1])!=Integer.MAX_VALUE && Operators(str.substring(idxEq+1,str.length()-1).trim())!=Integer.MAX_VALUE)
			Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(arr[0])].set(Operators(arr[1]),Operators(str.substring(idxEq+1,str.length()-1).trim()));
		
		else if(2<arr.length && Operators(arr[1])!=Integer.MAX_VALUE && arr[2].contains("nextInt()"))
			Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(arr[0])].set(Operators(arr[1]),scan.nextInt());
		
		String LastMethod = NowInMethod;
		 for(int k=0 ; k<arrName[FuncName.indexOf(NowInMethod)][nowInBlock].size() ; k++)//FuncRetunArr
		  for(int j=0 ; j<FuncName.size() ; j++)
			if(FuncKind.get(j).equals("int[]") && arr[0].startsWith(arrName[FuncName.indexOf(NowInMethod)][nowInBlock].get(k)) && arr[1].startsWith(FuncName.get(j)))
			{
				checkGiveVariFunc(lines.get(FuncLine.get(j)),arr[1],FuncName.get(j),LastMethod);
				int LineRes = lineM;
				ReturnValArr = new int [Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(arr[0])].size()];
				FuncReturnArr(FindBracketBlock(FuncLine.get(j),lines));
				lineM = LineRes;
				NowInMethod = LastMethod;
				
				for(int idx=0 ; idx<ReturnValArr.length ; idx++)
					Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(arr[0])].set(idx,ReturnValArr[idx]);
			}
	}
	public static int Operators(String oprant)
	{
		int res=Integer.MAX_VALUE,idxOp=0;
		if(BeInt(oprant))
			return Integer.parseInt(oprant);
		
		if(LinkedListGetItem(oprant)!=Integer.MAX_VALUE)
			return LinkedListGetItem(oprant);
		
		String LastMethod = NowInMethod;
		if(oprant.contains("("))
		for(int j=0 ; j<FuncName.size() ; j++)
			if(oprant.startsWith(FuncName.get(j)) && FuncKind.get(j).equals("int"))
			{
				checkGiveVariFunc(lines.get(FuncLine.get(j)),oprant,FuncName.get(j),LastMethod);
				int LineRes = lineM;
				res = FuncReturnInt(FindBracketBlock(FuncLine.get(j),lines));
				lineM = LineRes;
				NowInMethod = LastMethod;
				break;
			}
		
		if(res==Integer.MAX_VALUE)
		for(int i=0 ; i<oprant.length() ; i++)
			if(oprant.charAt(i)=='+' || oprant.charAt(i)=='-' || oprant.charAt(i)=='*' || oprant.charAt(i)=='/')
			{
					idxOp=i;
					if(BeInt(oprant.substring(0,idxOp))) 
						res=Integer.parseInt(oprant.substring(0,idxOp));
					
					else if(LinkedListGetItem(oprant.substring(0,idxOp))!=Integer.MAX_VALUE)
						res=LinkedListGetItem(oprant.substring(0,idxOp));
					
					else if(ArrGetItem(oprant.substring(0,idxOp))!=Integer.MAX_VALUE)
						res=ArrGetItem(oprant.substring(0,idxOp));
					break; 
			}
		for(int i=idxOp ; i<oprant.length() ; i++)
		{
			if(oprant.charAt(i)=='+')
			{
					for(int j=idxOp+1 ; j<=oprant.length() ; j++)
					{
						if(j==oprant.length() || oprant.charAt(j)=='+' || oprant.charAt(j)=='-' || oprant.charAt(j)=='*' || oprant.charAt(j)=='/')
						{
							if(BeInt(oprant.substring(idxOp+1,j))) 
								res+=Integer.parseInt(oprant.substring(idxOp+1,j));
							
							else if(LinkedListGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res+=LinkedListGetItem(oprant.substring(idxOp+1,j));
							
							else if(ArrGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res+=ArrGetItem(oprant.substring(idxOp+1,j));
							idxOp=j;
							break;
						}
					}
			}
			else if(oprant.charAt(i)=='-')
			{
					for(int j=idxOp+1 ; j<=oprant.length() ; j++)
					{
						if(j==oprant.length() || oprant.charAt(j)=='+' || oprant.charAt(j)=='-' || oprant.charAt(j)=='*' || oprant.charAt(j)=='/')
						{
							if(BeInt(oprant.substring(idxOp+1,j))) 
								res-=Integer.parseInt(oprant.substring(idxOp+1,j));
							
							else if(LinkedListGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res-=LinkedListGetItem(oprant.substring(idxOp+1,j));
							
							else if(ArrGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res-=ArrGetItem(oprant.substring(idxOp+1,j));
							idxOp=j;
							break;
						}
					}
			}
			else if(oprant.charAt(i)=='*')
			{
					for(int j=idxOp+1 ; j<=oprant.length() ; j++)
					{
						if(j==oprant.length() || oprant.charAt(j)=='+' || oprant.charAt(j)=='-' || oprant.charAt(j)=='*' || oprant.charAt(j)=='/')
						{
							if(BeInt(oprant.substring(idxOp+1,j))) 
								res*=Integer.parseInt(oprant.substring(idxOp+1,j));
							
							else if(LinkedListGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res*=LinkedListGetItem(oprant.substring(idxOp+1,j));
							
							else if(ArrGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res*=ArrGetItem(oprant.substring(idxOp+1,j));
							idxOp=j;
							break;

						}
					}
			}
			else if(oprant.charAt(i)=='/')
			{
					for(int j=idxOp+1 ; j<=oprant.length() ; j++)
					{
						if(j==oprant.length() || oprant.charAt(j)=='+' || oprant.charAt(j)=='-' || oprant.charAt(j)=='*' || oprant.charAt(j)=='/')
						{
							if(BeInt(oprant.substring(idxOp+1,j))) 
								res/=Integer.parseInt(oprant.substring(idxOp+1,j));
							
							else if(LinkedListGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res/=LinkedListGetItem(oprant.substring(idxOp+1,j));
							
							else if(ArrGetItem(oprant.substring(idxOp+1,j))!=Integer.MAX_VALUE)
								res/=ArrGetItem(oprant.substring(idxOp+1,j));
							idxOp=j;
							break;
						}
					}
			}
		}
		if(res==Integer.MAX_VALUE && ArrGetItem(oprant)!=Integer.MAX_VALUE)
			return ArrGetItem(oprant);
		return res;
	}
	public static void checkInBlock(LinkedList<String> BlockLines)
	{
		ReturnValInt = Integer.MAX_VALUE;
		StringTokenizer method = new StringTokenizer(BlockLines.get(lineM)," (");
		String [] methodTokens = new String[method.countTokens()]; 
		
		for(int i=0 ; i<methodTokens.length ; i++)
			methodTokens[i] = method.nextToken();
		
		if(methodTokens.length>=4 && methodTokens[0].equals("public"))
		{
			if(methodTokens[3].equals("[]"))
				NowInMethod = methodTokens[4];
			else
				NowInMethod = methodTokens[3];
		}
		RemoveBlock(BlockLines);
		
		if(BlockLines.get(lineM).startsWith("System.out.println"))
			Print(BlockLines.get(lineM));
		
		if(BlockLines.get(lineM).startsWith("return"))
		{
			ReturnValInt = ReturnInt(BlockLines,lineM);
			
			if(ReturnValInt==Integer.MAX_VALUE)
				ReturnArr(BlockLines);
		}
		if(!BlockLines.get(lineM).startsWith("int"))	
			GiveValue(BlockLines.get(lineM));
		
		if(BlockLines.get(lineM).startsWith("if"))
		{
			if(!BlockLines.get(lineM).contains("(") && BlockLines.get(lineM).contains(")"))
				System.out.println("error: if must have condition sentence.");
			if(BlockLines.get(lineM).contains("while"))
				System.out.println("error: while must have condition sentence.");
			checkIF(BlockLines);
		}
		
		if(lineM>=2 && IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM-2)] && BlockLines.get(lineM).startsWith("else if"))
		{
			IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
			lineM+=FindBracketBlock(lineM,BlockLines).size();
		}
		if(lineM>=2 && !IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM-2)] && BlockLines.get(lineM).startsWith("else if"))
			checkIF(BlockLines);
		
		if(lineM>=2 && IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM-2)] && BlockLines.get(lineM).startsWith("else"))
		{
			IfElse[FuncName.indexOf(NowInMethod)][lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM+1)]=true;
			lineM+=FindBracketBlock(lineM,BlockLines).size();
		}
		if((BlockLines.get(lineM).startsWith("int[]") || BlockLines.get(lineM).startsWith("int []")))
			checkArray(BlockLines.get(lineM));
		
		int nowInBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM);
		
		for(int Befor=0 ; nowInBlock-Befor>=0 ; Befor++)
			for(int j=0 ; j<arrName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].size() ; j++)
				if(BlockLines.get(lineM).startsWith(arrName[FuncName.indexOf(NowInMethod)][nowInBlock-Befor].get(j)))
					ReadArray(BlockLines.get(lineM),nowInBlock-Befor);	
		
		if(BlockLines.get(lineM).startsWith("int"))
			checkInt(BlockLines.get(lineM));
		
		if(BlockLines.get(lineM).startsWith("while"))
		{
			if(!BlockLines.get(lineM).contains("(") && BlockLines.get(lineM).contains(")"))
				System.out.println("error: while must have condition sentence.");
			if(BlockLines.get(lineM).contains("if"))
				System.out.println("error: if must have condition sentence.");
			checkWhile(BlockLines);
		}
		
		String LastMethod = NowInMethod;
		for(int i=0 ; i<FuncName.size() ; i++)
			if(BlockLines.get(lineM).startsWith(FuncName.get(i)) && FuncKind.get(i).equals("void"))
			{
				checkGiveVariFunc(lines.get(FuncLine.get(i)),BlockLines.get(lineM),FuncName.get(i),LastMethod);
				
				int LineRes = lineM;
				for(lineM=0 ;lineM<FindBracketBlock(FuncLine.get(i),lines).size() ; lineM++)
				{
					if(FindBracketBlock(FuncLine.get(i),lines).get(lineM).trim().equals("return;"))
						break;
					checkInBlock(FindBracketBlock(FuncLine.get(i),lines));
				}
				lineM = LineRes;
				NowInMethod = LastMethod;
				break;
			}
	}
	public static void RemoveBlock(LinkedList<String> BlockLines)
	{
		for(int j=0 ; j<BlockLines.get(lineM).length() ; j++)
			if(BlockLines.get(lineM).charAt(j)=='}')
			{
				int RemoveBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM-1);
				while(!varName[FuncName.indexOf(NowInMethod)][RemoveBlock].isEmpty())
				{
					varName[FuncName.indexOf(NowInMethod)][RemoveBlock].remove();
					varVal[FuncName.indexOf(NowInMethod)][RemoveBlock].remove();
				}
				while(!arrName[FuncName.indexOf(NowInMethod)][RemoveBlock].isEmpty())
					arrName[FuncName.indexOf(NowInMethod)][RemoveBlock].remove();
			
				for(int i=0 ;RemoveBlock<Arrays.length &&  i<Arrays[RemoveBlock].length ; i++)
					while(!Arrays[FuncName.indexOf(NowInMethod)][RemoveBlock][i].isEmpty())
						Arrays[FuncName.indexOf(NowInMethod)][RemoveBlock][i].remove();
			}
	}
	public static int ReturnInt(LinkedList<String> Blocklines,int Line)
	{
		String str = Blocklines.get(Line).substring(7,Blocklines.get(Line).length()-1);
		
		if(Operators(str)!=Integer.MAX_VALUE)
			return Operators(str);
		return Integer.MAX_VALUE;
	}
	public static void checkArray(String str)
	{
		boolean ok = true;
		StringTokenizer StArr = new StringTokenizer(str," ;="); 
		String [] arr = new String[StArr.countTokens()];
		
		for(int x=0 ; x<arr.length ; x++)
			arr[x]=StArr.nextToken().trim();
		
		if(!(arr[0].equals("int") && arr[1].equals("[]") && arr[3].equals("new") && arr[4].equals("int") && arr[5].charAt(0)=='[' && 
				(BeInt(arr[5].substring(1,arr[5].length()-1)) || LinkedListGetItem(arr[5].substring(1,arr[5].length()-1))!=Integer.MAX_VALUE) && 
						arr[5].charAt(arr[5].length()-1)==']'))
		{
			ok=false;
			System.out.println("error in line :"+lineM);
		}
		if(ok)
		{
			int size=-1,nowInBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM);
			arrName[FuncName.indexOf(NowInMethod)][nowInBlock].add(arr[2]);
			
			if(Operators(arr[5].substring(1,arr[5].length()-1))!=Integer.MAX_VALUE)
				size = Operators(arr[5].substring(1,arr[5].length()-1));
			
			for(int i=0 ; i<size ; i++)
				Arrays[FuncName.indexOf(NowInMethod)][nowInBlock][arrName[FuncName.indexOf(NowInMethod)][nowInBlock].indexOf(arr[2])].add(0);
		}
	}
	public static void checkMethodsMain()
	{
		for(int line=0 ; line<lines.size() ; line++)
			if(lines.get(line).startsWith("public static "))//Function
			{
				if(lines.get(line).equals("Scanner scan = new Scanner(System.in);") || lines.get(line).equals("Scanner scan=new Scanner(System.in);"));
					visScanner=true;
				
				if(lines.get(line).startsWith("int(") || lines.get(line).startsWith("void(") || lines.get(line).startsWith("int[](")) 
					System.out.println("Methods must be public static.");
				
				if(lines.get(line).equals("public static void main(String[] args)"))
					lineMain=line;
				
					FuncAddKindNameLine(line);
			}
		if(lineMain==-1)
			System.out.println("There is no Main.");

	}
	public static void checkBracket()//for functions that you give Block of every line
	{
		for(int Func=0 ; Func<FuncName.size() ; Func++)
		{
		int BracketOpen = -1,NowInBlock = 0;
		  for(int line=0 ; line< FindBracketBlock(FuncLine.get(Func),lines).size() ; line++)
		  {
			for(int i=0 ; i<FindBracketBlock(FuncLine.get(Func),lines).get(line).length() ; i++)
			{
				if(FindBracketBlock(FuncLine.get(Func),lines).get(line).charAt(i)=='{')
				{
					int AdvBlock = NowInBlock;
					BracketOpen++;
					NowInBlock=BracketOpen;
					PrevBlock[Func][NowInBlock] = AdvBlock;
				}
				if(FindBracketBlock(FuncLine.get(Func),lines).get(line).charAt(i)=='}')
				{
					NowInBlock = PrevBlock[Func][NowInBlock];
				}
			}
			if(NowInBlock<0)
				NowInBlock = 0;
			lineFuncBlock[Func].add(NowInBlock);
			
		  }
		}
	}	
	public static boolean BeInt(String x)
	{
		int i=0 ;
		if(x.charAt(0)=='-')
			i=1;
		for(;i<x.length() ; i++)
		{
			boolean ok=false;
			for(int num=0 ; num<10 ; num++)
				if(x.charAt(i)==(num+"").charAt(0))
					ok=true;
			if(!ok)
				return false;
		}
		return true;
	}
	public static int LinkedListGetItem(String x)
	{
		for(int Block=0 ; Block<varName.length ; Block++)
			for(int j=0 ; j<varName[FuncName.indexOf(NowInMethod)][Block].size() ; j++)
				if(x.equals(varName[FuncName.indexOf(NowInMethod)][Block].get(j)))
					return varVal[FuncName.indexOf(NowInMethod)][Block].get(j);
		return Integer.MAX_VALUE;
	}
	public static int ArrGetItem(String ArrNameIdx)
	{
		if(!ArrNameIdx.contains("["))
			return Integer.MAX_VALUE;
		
		StringTokenizer Str = new StringTokenizer(ArrNameIdx,"[];");
		String [] Arr = new String[Str.countTokens()];
		for(int i=0 ; i<Arr.length ; i++)
			Arr[i] = Str.nextToken();
		
		for(int Block=0 ; Block<Arrays[FuncName.indexOf(NowInMethod)].length ; Block++)
		{
			if(arrName[FuncName.indexOf(NowInMethod)][Block].contains(Arr[0]))
			if(Arrays[FuncName.indexOf(NowInMethod)][Block][arrName[FuncName.indexOf(NowInMethod)][Block].indexOf(Arr[0])].size()!=0)
				if(Operators(Arr[1])<Arrays[FuncName.indexOf(NowInMethod)][Block][arrName[FuncName.indexOf(NowInMethod)][Block].indexOf(Arr[0])].size())
					return Arrays[FuncName.indexOf(NowInMethod)][Block][arrName[FuncName.indexOf(NowInMethod)][Block].indexOf(Arr[0])].get(Operators(Arr[1]));
		}
		return Integer.MAX_VALUE;
	}
	public static void checkInt(String str)
	{
		scan = new Scanner(System.in);
		
		if(str.startsWith("int[]") || str.startsWith("int []") || str.startsWith("[]"))
			return;
		
		StringTokenizer stINT = new StringTokenizer(str," ;,=");
		String[] arrINT=new String[stINT.countTokens()];
		int nowInBlock = lineFuncBlock[FuncName.indexOf(NowInMethod)].get(lineM);
		
		for(int x=0 ; x<arrINT.length ; x++)
			arrINT[x]=stINT.nextToken().trim();
		
		for(int idx=1 ; idx<arrINT.length ; idx++)
		{
				if(beName(arrINT[idx]) && idx+1<arrINT.length && Operators(arrINT[idx+1])!=Integer.MAX_VALUE)
				{
					varName[FuncName.indexOf(NowInMethod)][nowInBlock].add(arrINT[idx]);
					varVal[FuncName.indexOf(NowInMethod)][nowInBlock].add(Operators(arrINT[idx+1]));
						idx++;
				}
				else if(beName(arrINT[idx]) && idx+1<arrINT.length && arrINT[idx+1].contains("nextInt()"))
				{
					if(!visScanner)
						System.out.println("error in definning Scanner.");
					varName[FuncName.indexOf(NowInMethod)][nowInBlock].add(arrINT[idx]);
					varVal[FuncName.indexOf(NowInMethod)][nowInBlock].add(scan.nextInt());
						idx++;
				}
				else if(beName(arrINT[idx]) && (idx+1>=arrINT.length || beName(arrINT[idx+1])))
				{
					varName[FuncName.indexOf(NowInMethod)][nowInBlock].add(arrINT[idx]);
					varVal[FuncName.indexOf(NowInMethod)][nowInBlock].add(Integer.MIN_VALUE);
				}
		}
	}
	public static void checkWhile(LinkedList<String> Blocklines)
	{
		String Sign = "";
		for(int i=0 ; i<Blocklines.get(lineM).length() ; i++)
		{
			if(Blocklines.get(lineM).substring(i,i+2).equals(">=") || Blocklines.get(lineM).substring(i,i+2).equals("<=") || 
					Blocklines.get(lineM).substring(i,i+2).equals("!=") || Blocklines.get(lineM).substring(i,i+2).equals("=="))
			{
				Sign = Blocklines.get(lineM).substring(i,i+2);
				break;
			}
			if(Blocklines.get(lineM).charAt(i)=='<' || Blocklines.get(lineM).charAt(i)=='>')
			{
				Sign = Blocklines.get(lineM).charAt(i)+"";
				break;
			}
		}
		StringTokenizer stWHILE = new StringTokenizer(Blocklines.get(lineM),"()<=>!" );
		String[] arrWHILE=new String[3];
		boolean ok = true;
		
		for(int x=0 ; x<arrWHILE.length ; x++)
			arrWHILE[x]=stWHILE.nextToken();
		
		for(int i=1 ; i<arrWHILE.length ; i++)
			if(Operators(arrWHILE[i])==Integer.MAX_VALUE)
			{
				System.out.println("error in line :"+lineM);                
				ok = false;
			}
		if(ok)
		{
				int LineRes = lineM+1;
				if(Sign.equals(">"))
					while(Operators(arrWHILE[1]) > Operators(arrWHILE[2]))
					for(lineM=LineRes ;  lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
						checkInBlock(Blocklines);
					}
				else if(Sign.equals("<"))
					while( Operators(arrWHILE[1]) < Operators(arrWHILE[2]))
					for(lineM=LineRes ;lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
						checkInBlock(Blocklines);
					}
				else if(Sign.equals("=="))
					while(Operators(arrWHILE[1]) == Operators(arrWHILE[2]))
					for(lineM=LineRes ; lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
					  checkInBlock(Blocklines);
					}
				else if(Sign.equals("!="))
					while(Operators(arrWHILE[1]) != Operators(arrWHILE[2]))
					for(lineM=LineRes ; lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
						checkInBlock(Blocklines);
					}
				else if(Sign.equals(">="))
					while(Operators(arrWHILE[1]) >= Operators(arrWHILE[2]))
					for(lineM=LineRes ; lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
						checkInBlock(Blocklines);
					}
				else if(Sign.equals("<="))
					while(Operators(arrWHILE[1]) <= Operators(arrWHILE[2]))
					for(lineM=LineRes ; lineM<LineRes+FindBracketBlock(LineRes-1,Blocklines).size()-2 ; lineM++)
					{
						checkInBlock(Blocklines);
					}
				}
			}
}	
class JavaCompiler extends Methods
{
	public static void main(String[] args)throws Exception
	{
		MethodRead1();
		MethodRead2();
		checkSyntax();
		checkBracket();
		
		for(lineM=0 ; lineM<FindBracketBlock(lineMain,lines).size() ; lineM++)
			checkInBlock(FindBracketBlock(lineMain,lines));
	}
}