/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Administrator
 */
public class Angular_encryption
{
    int div = 9;
    String enc_string;
    long e_time, d_time, c_time, ex_time;
    public long getEncryptionTime()
    {
        return e_time;
    }
    public long getDecryptionTime()
    {
        return d_time;
    }
    public long getCompressionTime()
    {
        return c_time;
    }
    public long getExtractionTime()
    {
        return ex_time;
    }
    public String getEncryptedString()
    {
        return enc_string;
    }
    public String encrypt(String str, int etype)
    {
        long start = System.currentTimeMillis();
        int l=str.length();
        String estr = "";
        String temp;
        switch(etype)
        {
            case 1:     temp = penc('p');
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        for(int i = 1 ; i <= l; i++)
                        {
                            temp = penc(str.charAt(i-1));   
                            estr += temp.charAt(2);
                            estr += temp.charAt(1);
                            estr += temp.charAt(0);
                        }
                        break;
            case 2:     temp = penc('q');
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        for(int i = 1 ; i <= l; i++)
                        {
                            temp = nenc(str.charAt(i-1));
                            estr += temp.charAt(2);
                            estr += temp.charAt(1);
                            estr += temp.charAt(0);
                        }
                        break;
            case 3:     temp = penc('r');
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        for(int i = 1 ; i <= l; i++)
                        {
                        if((i-1)%2==0)
                            temp = penc(str.charAt(i-1));
                        else
                            temp = nenc(str.charAt(i-1));
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        }
                        break;
            case 4:     temp = penc('s');
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        for(int i = 1 ; i <= l; i++)
                        {
                        if((i-1)%2==0)
                            temp = nenc(str.charAt(i-1));
                        else
                            temp = penc(str.charAt(i-1));
                        estr += temp.charAt(2);
                        estr += temp.charAt(1);
                        estr += temp.charAt(0);
                        }
        }
        e_time = System.currentTimeMillis() - start;
        return compress(enc_string = estr);
    }
    //</editor-fold>
    public String decrypt(String cstr)
    //<editor-fold defaultstate="collapsed" desc="{...}
    {
        String estr = extract(cstr);
        long start = System.currentTimeMillis();
        int l = estr.length();
        String temp = estr.substring(0, 3), str = "";
        temp += temp.charAt(1);
        temp += temp.charAt(0);
        int i=Integer.parseInt(temp.substring(2, 5));
        if(i > 288)
            i = 4;
	else if(i > 279)
		i = 3;
	else if(i > 270)
		i = 2;
	else
		i = 1;
	switch(i)
	    {
		case 1: for(i = 3; i < l; i += 3)
                            {
                                temp = estr.substring(i, i + 3);
                                temp += temp.charAt(1);
                                temp += temp.charAt(0);
                                str += pdec(Integer.parseInt(temp.substring(2, 5)));
                            }
                         break;
		case 2: for(i = 3; i < l; i += 3)
                            {
                                temp = estr.substring(i, i + 3);
                                temp += temp.charAt(1);
                                temp += temp.charAt(0);
                                str += ndec(Integer.parseInt(temp.substring(2, 5)));
                            }
                         break;
		case 3: for(i = 3; i < l; i += 3)
                            {
                                temp = estr.substring(i, i + 3);
                                temp += temp.charAt(1);
                                temp += temp.charAt(0);
                                if ((i / 3) % 2 == 1)
                                    str += pdec(Integer.parseInt(temp.substring(2, 5)));
                                else
                                    str += ndec(Integer.parseInt(temp.substring(2, 5)));
                            }
                         break;
		case 4: for(i = 3; i < l; i += 3)
                            {
                                temp = estr.substring(i, i + 3);
                                temp += temp.charAt(1);
                                temp += temp.charAt(0);
                                if ((i / 3) % 2 == 1)
                                    str += ndec(Integer.parseInt(temp.substring(2, 5)));
                                else
                                    str += pdec(Integer.parseInt(temp.substring(2, 5)));
                            }
		}   
        d_time = System.currentTimeMillis() - start;
        return str;
    }
    //</editor-fold>
    private String compress(String estr)
    //<editor-fold defaultstate="collapsed" desc="{...}
    {
        long start = System.currentTimeMillis();
        String cstr = "";
        int l = estr.length()-1;
        for(int i = 0; i < l; i += 2)
        { 
            char a = estr.charAt(i), b = estr.charAt(i+1);
            a++;
            b++;
            cstr += (char)((byte)(a-48) << 4 | ((byte)b-48));
        }
        if((l % 2) == 0)
        {
            char a = estr.charAt(l);
            a++;
            cstr += (char)((byte)a-48 );
        }
        //System.out.println("compressed : " + cstr);
        c_time = System.currentTimeMillis() - start;
        return cstr;
    }
    //</editor-fold>
    private String extract(String cstr)
    //<editor-fold defaultstate="collapsed" desc="{...}
    {
        long start = System.currentTimeMillis();
        int l=cstr.length();
        String estr = "";
        for(int i = 0; i < l; i++)
        {
            
            estr += (int)((cstr.charAt(i) & 240) >> 4) - 1;
            estr += (int)(cstr.charAt(i) & 15) - 1;
        }
        if((cstr.charAt(l-1) & 240) == 0 )
        {
            estr = estr.substring(0, estr.length()-3);
            estr += (int)(cstr.charAt(cstr.length()-1) & 15) - 1;
        }
        //System.out.println("Extracted : " + estr);
        ex_time = System.currentTimeMillis() - start;
        return estr;
    }
    //</editor-fold>
    private String penc(char ch)
    {
    byte b = (byte)Character.toUpperCase(ch);
    if(b >= 65 && b <= 90)
		return String.format("%03d",((b - 50) * div) - ((int)(Math.random()*10 % div)));
    else if(b >= 48 && b <= 57)
		return String.format("%03d",((b - 44) * div) - ((int)(Math.random()*10 % div)));
	else
		{
			switch(ch)
				{
					case ' ': return String.format("%03d",9 - ((int)(Math.random()*10 % div)));
					case ',': return String.format("%03d",18 - ((int)(Math.random()*10 % div)));
					case '.': return String.format("%03d",27 - ((int)(Math.random()*10 % div)));
					case '?': return String.format("%03d",126 - ((int)(Math.random()*10 % div)));
					default:  System.out.print("\nFound undefined symbol - " + ch + "\nProgram terminated\n");
                                                  return "361";
		       			     	
				}
		}
}  
    private char pdec(int num)
    //<editor-fold defaultstate="collapsed" desc="{...}
{
    
    int j;
    if(num>-1 && num<10)
        return ' ';
    else if(num>9 && num<19)
        return ',';
    else if(num>18 && num<28)
        return '.';
    else if(num>117 && num<127)
        return '?';
    else if(num>126 && num<361)
    {
        j=15;
        while(j<41)
        {
            if(num<=(j*div) && num>((j-1)*div))
                return (char)(j+50);
            j++;
        }
    }
    else
    {
        j=4;
        while(j<14)
        {
            if(num<=(j*div) && num>((j-1)*div))
                return (char)(j+44);
            j++;
        }
    }
    return '_';
}
//</editor-fold>
    private String nenc(char ch)
    //<editor-fold defaultstate="collapsed" desc="{...}">
    {
        byte b = (byte)Character.toUpperCase(ch);
        if(b >= 65 && b <= 90)
            return String.format("%03d",((91 - b) * div) - ((int)(Math.random()*10 % div)));
        else if(b >= 48 && b <= 57)
            return String.format("%03d",((85 - b) * div) - ((int)(Math.random()*10 % div)));
        else
        {
            switch(ch)
            {
                case ' ': return String.format("%03d", 360 - ((int)(Math.random()*10 % div)));
                case ',': return String.format("%03d", 351 - ((int)(Math.random()*10 % div)));
                case '.': return String.format("%03d", 342 - ((int)(Math.random()*10 % div)));
                case '?': return String.format("%03d", 243 - ((int)(Math.random()*10 % div)));
                default:  System.out.print("\nFound undefined symbol - " + ch + "\nProgram terminated\n");
                          return "361";
            }
        
        }
    }
    //</editor-fold>
    private char ndec(int num)
    //<editor-fold defaultstate="collapsed" desc="{...}">
    {
        int j;
        if(num>351 && num<361)
            return ' ';
        else if(num>342 && num<352)
            return ',';
        else if(num>333 && num<343)
            return '.';
        else if(num>234 && num<244)
            return '?';
        else if(num>-1 && num<235)
        {
            j=26;
            while(j>0)
            {
                if(num<=(j*div) && num>((j-1)*div))
                    return (char)(91-j);
                j--;
            }
        }
        else
        {
            j=37;
            while(j>27)
            {
                if(num<=(j*div) && num>((j-1)*div))
                    return (char)(85-j);
                j--;
            }
        }
        return '_';
    }
    //</editor-fold>    
}
/*public class Angular_encryption_test{

    /**
     * @param args the command line arguments
     *//*
    public static void main(String[] args) {
        Angular_encryption obj = new Angular_encryption();
       System.out.println(obj.decrypt(obj.encrypt("abcdefghijklmnopqrs_tuvwxyz0123456789,. ?the quick brown fox jumps upon the little lazy dog. the quick brown fox jumps upon the little lazy dog.abcdefghijklmnopqrs_tuvwxyz0123456789,. ?the quick brown fox jumps upon the little lazy dog. the quick brown fox jumps upon the little lazy dog.abcdefghijklmnopqrs_tuvwxyz0123456789,. ?the quick brown fox jumps upon the little lazy dog. the quick brown fox jumps upon the little lazy dog.abcdefghijklmnopqrs_tuvwxyz0123456789,. ?the quick brown fox jumps upon the little lazy dog. the quick brown fox jumps upon the little lazy dog.abcdefghijklmnopqrs_tuvwxyz0123456789,. ?the quick brown fox jumps upon the little lazy dog. the quick brown fox jumps upon the little lazy dog.", 1)));
       System.out.println(obj.getEncryptedString());
       System.out.println("Encryption time : " + obj.getEncryptionTime());
       System.out.println("Compression time : " + obj.getCompressionTime());
       System.out.println("Decryption time : " + obj.getDecryptionTime());
       System.out.println("Extraction time : " + obj.getExtractionTime());
       
       
        //System.out.println(obj.nenc(','));
    }
}*/
