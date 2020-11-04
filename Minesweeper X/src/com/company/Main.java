package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main{
    public static void main(String[] args) {
        System.out.print("How many mines do you want on the field? ");
        Scanner scanner = new Scanner(System.in);
        int mineNumber = scanner.nextInt();
        char[] board = new char[90];
        Integer[] minePos = new Integer[mineNumber];
        init(board);
        int absolutePos;
        String command;
        boolean firstTime = true;
        char[] copy = new char[90] ;
        do {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            do {
                int posX = scanner.nextInt();
                int posY = scanner.nextInt();
                absolutePos = (posY-1)*10+posX-1;
                command = scanner.next();
            }while(board[absolutePos]=='/');
            if(command.equals("free")){
                if(firstTime){
                    randomNumbers(mineNumber,minePos,absolutePos);
                    findPossibility(board,minePos,mineNumber);
                    for(int l=0;l<90;l++){
                        copy[l]=board[l];
                    }
                    firstTime = false;
                }
                if(Arrays.asList(minePos).contains(absolutePos)){
                    System.out.println("You stepped on a mine and failed!");
                    //+pos for animations.
                    showBombs(board,copy);
                    return;
                }
                else{
                    if(board[absolutePos]=='0'){
                        board[absolutePos] = '/';
                        revealSlashes(board,absolutePos);
                    }
                    else if(board[absolutePos]=='*'){
                        board[absolutePos] = copy[absolutePos];
                    }
                    else{
                        board[absolutePos] = 'k';
                    }
                }
            }
            else if(command.equals("mine")){
                if(board[absolutePos]!='*'){
                    board[absolutePos]='*';
                }
                else if(!firstTime){
                    board[absolutePos]=copy[absolutePos];
                }
                else{
                    board[absolutePos] = '0';
                }
            }
            printBoard(board,copy,firstTime);
        }while(!control(board,minePos,mineNumber));
        System.out.println("Congratulations! You found all mines!");
    }
    public static void init(char[] board){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i=0;i<90;i++){
            if(i%10==0){
                System.out.print(i/10+1+"|.");
                board[i] = '0';
            }
            else if(i%10==9){
                System.out.println("|");
                board[i] = '\n';
            }
            else{
                System.out.print(".");
                board[i] = '0';
            }
        }
        System.out.println("-|---------|");
    }
    public static void randomNumbers(int numberMines,Integer[] minePos,int pos) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=0; i<90; i++) {
            if((i+1)%10!=0&&pos!=i&&!(i>=pos-11&&i<=pos-9)&&pos+1!=i&&pos-1!=i&&!(i>=pos+9&&i<=pos+11)){
                list.add(i);
            }
        }
        Collections.shuffle(list);
        for (int i=0; i<numberMines; i++) {
            minePos[i] = list.get(i);
        }
    }
    public static void findPossibility(char[] result,Integer[] minePos,int mineNumber){
        for(int i=0;i<mineNumber;i++){
            result[minePos[i]] ='X';
        }
        int start, end;
        for(int i=0;i<mineNumber;i++){
            int pos = minePos[i];
            int[] ar = new int[2];
            findStartEnd(pos,ar);
            start = ar[0];
            end = ar[1];
            place(result,pos,start,end);
        }
    }
    public static void findStartEnd(int pos,int[] ar){
        if(pos-11>=0){
            ar[0] = pos-11;
        }
        else if(pos-10==0){
            ar[0] = 0;
        }
        else if(pos-1>=0){
            ar[0] = pos-1;
        }
        else {
            ar[0] = pos;
        }
        if(pos+12<90){
            ar[1] = pos+12;
        }
        else if(pos+11==90-1){
            ar[1] = pos+11;
        }
        else if(pos+2<90){
            ar[1] = pos+2;
        }
        else {
            ar[1] = pos+1;
        }
    }
    public static void place(char[] result,int pos,int start,int end){
        for(int j = start;j<end;j++){
            if(result[j]!='X'&&(j+1)%10!=0){
                if(pos>=10&&pos<=79&&pos%10!=0&&pos%10!=9&&(j+1)%10!=0){
                    if((j>=pos-11&&j<=pos-9)||(pos-1==j)||(pos+1==j)||(pos+9<=j&&pos+11>=j)){
                        result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                    }
                }
                else if(pos==0&&((pos+1==j)||(pos+10<=j&&pos+11>=j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos==9&&((pos-1==j)||(pos+9<=j&&pos+10>=j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos==80&&((j>=pos-10&&j<=pos-9)||(pos+1==j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos==89&&((j>=pos-11&&j<=pos-10)||(pos-1==j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos%10==0&&((j>=pos-10&&j<=pos-9)||(pos+1==j)||(pos+10<=j&&pos+11>=j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos%10==9&&((j>=pos-11&&j<=pos-10)||(pos-1==j)||(pos+9<=j&&pos+10>=j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos/10==0&&((pos-1==j)||(pos+1==j)||(pos+9<=j&&pos+11>=j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
                else if(pos/10==8&&((j>=pos-11&&j<=pos-9)||(pos-1==j)||(pos+1==j))){
                    result[j] = (char)(Character.getNumericValue(result[j])+1 +(int)'0');
                }
            }
        }
    }
    public static boolean control(char[] board,Integer[] minePos,int mineNumber){
        Pattern pattern = Pattern.compile("//d");
        int counter = 0;
        int mineCounter = 0;
        int wrongCounter = 0;
        boolean cond = true;
        for(int i=0;i<90;i++){
            if(pattern.matcher(Character.toString(board[i])).matches()||board[i]=='/'){
                counter++;
            }
            if(board[i]=='*'){
                for(int j = 0;j<mineNumber;j++){
                    if(Arrays.asList(minePos).contains(i)){
                        mineCounter++;
                        cond = false;
                    }
                }
                if(cond){
                    wrongCounter++;
                }
            }
        }
        if(counter == 90-mineNumber){
            return true;
        }
        else return mineCounter == mineNumber && wrongCounter == 0;
    }
    public static void revealSlashes(char[] board,int pos){
        int[] ar=new int[8];
        encircle(board,pos,ar);
        for(int i=0;i<8;i++){
            if(ar[i]!=-1&&board[ar[i]]=='0'){
                board[ar[i]]='/';
                revealSlashes(board,ar[i]);
            }
        }
    }
    public static void encircle(char[] result,int pos,int[] ar){
        for(int i=0;i<8;i++){
            ar[i] =-1;
        }
        int[] arN = new int[2];
        findStartEnd(pos,arN);
        int start = arN[0];
        int end = arN[1];
        int counter = 0;
        for(int j = start;j<end;j++){
            if(result[j]!='X'&&(j+1)%10!=0){
                if(pos>=10&&pos<=79&&pos%10!=0&&pos%10!=9&&(j+1)%10!=0){
                    if((j>=pos-11&&j<=pos-9)||(pos-1==j)||(pos+1==j)||(pos+9<=j&&pos+11>=j)){
                        ar[counter++]=j;
                    }
                }
                else if(pos==0&&((pos+1==j)||(pos+10<=j&&pos+11>=j))){
                    ar[counter++]=j;
                }
                else if(pos==9&&((pos-1==j)||(pos+9<=j&&pos+10>=j))){
                    ar[counter++]=j;
                }
                else if(pos==80&&((j>=pos-10&&j<=pos-9)||(pos+1==j))){
                    ar[counter++]=j;
                }
                else if(pos==89&&((j>=pos-11&&j<=pos-10)||(pos-1==j))){
                    ar[counter++]=j;
                }
                else if(pos%10==0&&((j>=pos-10&&j<=pos-9)||(pos+1==j)||(pos+10<=j&&pos+11>=j))){
                    ar[counter++]=j;
                }
                else if(pos%10==9&&((j>=pos-11&&j<=pos-10)||(pos-1==j)||(pos+9<=j&&pos+10>=j))){
                    ar[counter++]=j;
                }
                else if(pos/10==0&&((pos-1==j)||(pos+1==j)||(pos+9<=j&&pos+11>=j))){
                    ar[counter++]=j;
                }
                else if(pos/10==8&&((j>=pos-11&&j<=pos-9)||(pos-1==j)||(pos+1==j))){
                    ar[counter++]=j;
                }
            }
        }
    }
    public static void showBombs(char[] board,char[] copy){
        Pattern pattern = Pattern.compile("\\d");
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i=0;i<90;i++){
            boolean isOkay = false;
            if(i%10==0){
                System.out.print(i/10+1+"|");
            }
            else if(i%10==9) {
                System.out.print("|");
            }
            if(board[i]=='0'){
                System.out.print(".");
            }
            else if(board[i]=='\n'){
                System.out.print(board[i]);
            }
            else if(board[i]=='X'||board[i]=='/'){
                System.out.print(board[i]);
            }
            else if(board[i]=='*'){
                int[] ar = new int[8];
                for(int k=0;k<8;k++){
                    ar[k] = -1;
                }
                encircle(board,i,ar);
                for(int j=0;j<8;j++){
                    if(ar[j]!=-1){
                        if(copy[ar[j]]=='X'&&pattern.matcher(Character.toString(copy[i])).matches()){
                            System.out.print(copy[i]);
                            board[i]=copy[i];
                            isOkay =true;
                            break;
                        }
                    }
                }
                for(int j=0;j<8;j++){
                    if(ar[j]!=-1){
                        if(!isOkay&&board[ar[j]]=='/'){
                            System.out.print("/");
                            board[i] = '/';
                            isOkay =true;
                            break;
                        }
                    }
                }
                if(!isOkay){
                    System.out.print(board[i]);
                }
            }
            else if(board[i]=='k'){
                System.out.print(copy[i]);
            }
            else if((pattern.matcher(Character.toString(board[i])).matches())&&Character.getNumericValue(board[i])!=0){
                int[] ar = new int[8];
                for(int k=0;k<8;k++){
                    ar[k] = -1;
                }
                encircle(board,i,ar);
                for(int j=0;j<8;j++){
                    if(ar[j]!=-1&&board[ar[j]]=='/'){
                        System.out.print(board[i]);
                        isOkay = true;
                        break;
                    }
                }
                if(!isOkay){
                    System.out.print(".");
                }
            }
        }
        System.out.println("-|---------|");
    }
    public static void printBoard(char[] board,char[] copy, boolean firstTime){
        Pattern pattern = Pattern.compile("\\d");
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i=0;i<90;i++){
            boolean isOkay = false;
            boolean is = false;
            if(i%10==0){
                System.out.print(i/10+1+"|");
            }
            else if(i%10==9) {
                System.out.print("|");
            }
            if(board[i]=='0'){
                int[] ar = new int[8];
                for(int k=0;k<8;k++){
                    ar[k] = -1;
                }
                encircle(board,i,ar);
                for(int j=0;j<8;j++){
                    if(ar[j]!=-1&&board[ar[j]]=='/'){
                        System.out.print(board[i]);
                        is =true;
                        break;
                    }
                }
                if(!is){
                    System.out.print(".");
                }
            }
            else if(board[i]=='\n'){
                System.out.print(board[i]);
            }
            else if (board[i]=='/'){
                System.out.print(board[i]);
            }
            else if(board[i]=='*'){
                if(copy[i]=='X'){
                    System.out.print("*");
                }
                else if(!firstTime){
                    int[] ar = new int[8];
                    for(int k=0;k<8;k++){
                        ar[k] = -1;
                    }
                    encircle(board,i,ar);
                    for(int j=0;j<8;j++){
                        if(ar[j]!=-1){
                            if(copy[ar[j]]=='X'&&pattern.matcher(Character.toString(copy[i])).matches()){
                                System.out.print(copy[i]);
                                board[i] = copy[i];
                                isOkay =true;
                                break;
                            }
                        }
                    }
                    for(int j=0;j<8;j++){
                        if(ar[j]!=-1){
                            if(!isOkay&&board[ar[j]]=='/'){
                                System.out.print("/");
                                board[i] = '/';
                                isOkay =true;
                                break;
                            }
                        }
                    }
                    if(!isOkay){
                        System.out.print("*");
                    }
                }
                else{
                    System.out.print("*");
                }
            }
            else if(board[i]=='k'){
                System.out.print(copy[i]);
            }
            else if((pattern.matcher(Character.toString(board[i])).matches())){
                int[] ar = new int[8];
                for(int k=0;k<8;k++){
                    ar[k] = -1;
                }
                encircle(board,i,ar);
                for(int j=0;j<8;j++){
                    if(ar[j]!=-1&&board[ar[j]]=='/'){
                        System.out.print(board[i]);
                        isOkay =true;
                        break;
                    }
                }
                if(!isOkay){
                    System.out.print(".");
                }
            }
            else if(board[i]=='X'){
                System.out.print(".");
            }
        }
        System.out.println("-|---------|");
    }
}