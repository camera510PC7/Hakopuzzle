import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hakopuzzle extends JFrame implements ActionListener,MouseListener{

  int x = 0,y = 0;
  int turn = 0, kind = 0,select = 0;
  Box field = new Box();//フィールド作成
  Block block = new Block();//ブロック生成
  Problem pro = new Problem();//問題ファイル読込み

  PuzzleView PV = new PuzzleView();
  public static void main(String argc[]){
    Hakopuzzle game = new Hakopuzzle("箱詰めパズル",1400,800); //ゲーム開始
    game.setVisible(true);
  }

  Hakopuzzle(String title,int width,int height){
    setTitle(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(width,height);
    setLocationRelativeTo(null);
    setResizable(false);
    //初期化
    Container CP = getContentPane();
    PV.setLayout(null);
    CP.add(PV);    

    PV.addMouseListener(this);

    PV.select_turn.addActionListener(this);
    PV.add_button.addActionListener(this);
    PV.delete_button.addActionListener(this);

    for(int i=0;i<7;i++){
      PV.mino[i].addActionListener(this);
    }

    PV.setBlock(block);
    PV.setBox(field);
    PV.setProblem(pro);
    PV.showBlockRemains();
    PV.showMessage("追加したいブロックを選択し、置きたい場所をクリックしてください");
  }

  public void actionPerformed(ActionEvent event){
    if(event.getSource() == PV.mino[0]){
        kind = 0;
    }
    if(event.getSource() == PV.mino[1]){
        kind = 1;
    }
    if(event.getSource() == PV.mino[2]){
        kind = 2;
    }
    if(event.getSource() == PV.mino[3]){
        kind = 3;
    }
    if(event.getSource() == PV.mino[4]){
        kind = 4;
    }
    if(event.getSource() == PV.mino[5]){
        kind = 5;
    }
    if(event.getSource() == PV.mino[6]){
        kind = 6;
    }
    if(event.getSource() == PV.select_turn){
        turn = (turn + 1) % 4;
    }
    if(event.getSource() == PV.delete_button){
      PV.delete_mode();
      PV.showMessage("削除したいブロックをクリックしてください");
      select = 1;
    }
    if(event.getSource() == PV.add_button){
      PV.add_mode();
      PV.showMessage("ブロックを選択し、置きたい場所をクリックしてください");
      select = 0;
    }

    PV.setSelect(select);
    PV.setBlockInfo(kind,turn);
    PV.showBlockRemains();
    PV.repaint();
  }

  public void mouseExited(MouseEvent e){}

  public void mouseEntered(MouseEvent e){}

  public void mousePressed(MouseEvent e){}
  
  public void mouseReleased(MouseEvent e){}

  public void mouseClicked(MouseEvent e){
    int delete_block_kind = -1;
    Point point = e.getPoint();
    x = (point.x-400)/80;
    y = (point.y-20)/80;
    //フィールド内をクリックした時
    if((point.x > 400)&&(point.x < 1360)&&(point.y > 20)&&(point.y < 660)){
      if(select == 0){
        if((pro.status(kind) == true)){
          if(field.setBlock(block.getBlock(kind,turn),x,y,block.blockStart[kind][turn][0],block.blockStart[kind][turn][1],kind) == 0){
            pro.remove(kind);
            PV.showMessage("");
          }else{
            PV.showMessage("ブロックを置くことができません");
          }
          PV.showBlockRemains();
          PV.repaint();
        }else{
            PV.showMessage("置けるブロックがありません");
        }
        if(field.checkEnd() == true){
          PV.showMessage("ゲームクリア！！");
          return;
        }
      }else if(select == 1){
        delete_block_kind = field.deleteBlock(x,y);
        if(delete_block_kind != -1){
          pro.add(delete_block_kind);
          PV.showMessage("");
        }else{
          PV.showMessage("ブロックがありません");
        }
        PV.showBlockRemains();
        PV.repaint();
      }
    }
  }
}

class PuzzleView extends JPanel {

  int x = 0,y = 0;
  int turn = 0, kind = 0,select = 0;
  Box field;
  Block block;
  Problem pro;

  ImageIcon icon[] = new ImageIcon[7];
  JButton mino[] = new JButton[7];

  JButton select_turn = new JButton("回転");

  JButton add_button = new JButton("追加");
  JButton delete_button = new JButton("削除");

  JLabel mode = new JLabel();
  JLabel BlkCount = new JLabel();
  JLabel message = new JLabel();

  PuzzleView(){

    int buttonCount = 0;
  
    for(int i=0;i<7;i++){
      icon[i] = new ImageIcon("./img/"+i+".png");
    }
    for(int i=0;i<3;i++){
      for(int j=0;j<3;j++){
        if(buttonCount > 6){
          break;
        }
        mino[buttonCount] = new JButton(icon[buttonCount]);
        mino[buttonCount].setBounds(45+110*j,250+110*i,95,95);
        this.add(mino[buttonCount]);
        buttonCount++;
      }
    }

    select_turn.setFont(new Font("メイリオ",Font.PLAIN,30));
    select_turn.setBounds(210,470,150,95);
    this.add(select_turn);

    add_button.setFont(new Font("メイリオ",Font.PLAIN,30));
    add_button.setBounds(50,650,150,95);
    this.add(add_button);
    add_button.setEnabled(false);

    delete_button.setFont(new Font("メイリオ",Font.PLAIN,30));
    delete_button.setBounds(230,650,150,95);
    this.add(delete_button);
    
    mode.setBounds(150,600,200,50);
    mode.setFont(new Font("メイリオ",Font.PLAIN,30));
    mode.setOpaque(true);
    mode.setText("モード切替");
    this.add(mode);

    BlkCount.setBounds(30,30,70,50);
    BlkCount.setFont(new Font("メイリオ",Font.PLAIN,18));
    BlkCount.setOpaque(true);
    this.add(BlkCount);

    message.setBounds(430,670,1000,80);
    message.setFont(new Font("メイリオ",Font.PLAIN,30));
    message.setOpaque(true);
    this.add(message);
  }


  public void paintComponent(Graphics g){

    int box[][] = field.getBox();
    int crt_block[][] = block.getBlock(kind,turn);

    for(int i=0;i<8;i++){
      for(int j=0;j<12;j++){
        if(box[i][j] != 0){
          g.setColor(Color.BLUE);
        }else{
          g.setColor(Color.WHITE);
        }
        g.fillRect(400+j*80,20+i*80,80,80);
      }
    }

    for(int i=0;i<4;i++){
      for(int j=0;j<4;j++){
        if(crt_block[i][j] != 0){
          if((i == block.blockStart[kind][turn][1])&&(j == block.blockStart[kind][turn][0])){
            g.setColor(Color.BLACK);
          }else{
            g.setColor(Color.BLUE);
          }
        }else{
          g.setColor(Color.WHITE);
        }
        g.fillRect(100+j*50,20+i*50,50,50);
      }
    }
  }

  void setBlockInfo(int kind,int turn){
    this.kind = kind;
    this.turn = turn;
  }

  void setSelect(int select){
    this.select = select;
  }
  
  void setBlock(Block block){
    this.block = block;
  }
  
  void setBox(Box field){
    this.field = field;
  }

  void setProblem(Problem pro){
    this.pro = pro;
  }
  void delete_mode(){
    add_button.setEnabled(true);
    delete_button.setEnabled(false);
    for(int i=0;i<7;i++){
      mino[i].setEnabled(false);
    }
    select_turn.setEnabled(false);
  }

  void add_mode(){
    add_button.setEnabled(false);
    delete_button.setEnabled(true);        
    for(int i=0;i<7;i++){
      mino[i].setEnabled(true);
    }
    select_turn.setEnabled(true);
  }

  void showBlockRemains(){
    BlkCount.setText(String.valueOf("残り:"+pro.getCount(kind)));
    repaint();
  }

  void showMessage(String m){
    message.setText(m);
    repaint();
  }

}

class Box{
  int cell[][] = new int[8][12];
  int oldCell[][] = new int[8][12];
  int blockCount = 0;


  ArrayList<Integer> blockList = new ArrayList<Integer>();
  ArrayList<Integer> blockKind = new ArrayList<Integer>();

  Box(){
    for(int i = 0; i < 8;i++){
      for(int j = 0;j<12;j++){
        cell[i][j] = 0;
      }
    }
  }

  int[][] getBox(){
    return cell;
  }

  int setBlock(int block[][],int x,int y,int sx,int sy,int kind){
    int blockPosition[] = new int[9];
    int position = 0;
    for(int i = 0; i < 8;i++){
      for(int j = 0;j<12;j++){
        oldCell[i][j] = cell[i][j];
      }
    }
    try{
      for(int i=sy;i<4;i++){
        for(int j=0;j<4;j++){
          if(block[i][j] != 0){
            cell[y+i-sy][x+j-sx] = cell[y+i-sy][x+j-sx] + block[i][j];  
            blockPosition[position] = x+j-sx;
            position++;
            blockPosition[position] = y+i-sy;
            position++;
          }
        }
      }
    }catch(ArrayIndexOutOfBoundsException e){
      //配列の範囲を超えてブロックを置こうとした場合
      for(int i = 0; i < 8;i++){
        for(int j = 0;j<12;j++){
          cell[i][j] = oldCell[i][j];
        }
      }
      return -1;
    }
    //ブロックを重ねて置こうとした場合
    if(checkCell(cell) == false){
      for(int i = 0; i < 8;i++){
        for(int j = 0;j<12;j++){
          cell[i][j] = oldCell[i][j];
        }
      }
      return -1;
    }
    blockCount++;
    for(int i=8*(blockCount-1);i<8*blockCount;i++){
      blockList.add(blockPosition[i%8]);
    }
    blockKind.add(kind);
    return 0;
  }
  
  boolean checkCell(int cell[][]){
    int ok = 0;
    for(int i=0;i<8;i++){
      for(int j=0;j<12;j++){
        if(cell[i][j] == 1 || cell[i][j] == 0){
          ok++;
        }
      }
    }

    if(ok == 96){
      return true;
    }
  
    return false;
  }

  int deleteBlock(int x,int y){
    int deleteNum = -1;
    int kind = -1;
    for(int i=0;i<blockList.size();i=i+2){
      if((blockList.get(i) == x)&&(blockList.get(i+1) == y)){
        deleteNum = (i / 8) + 1;
        break;
      }
    }

    if(deleteNum == -1){
      return -1;
    }

    for(int i=8*(deleteNum-1);i<8*deleteNum;i++){
      if(i%2==0){
        cell[blockList.get(8*(deleteNum-1)+1)][blockList.get(8*(deleteNum-1))] = 0;
      }
      blockList.remove(8*(deleteNum-1));
    }
    kind = blockKind.get(deleteNum-1);
    blockKind.remove(deleteNum-1);
    return kind;
  }

  boolean checkEnd(){
    int count = 0;
    for(int i=0;i<8;i++){
      for(int j=0;j<12;j++){
        if(cell[i][j]==1){
          count++;
        }
      }
    }

    if(count == 96){
      return true;
    }

    return false;
  }

}
class Problem{
  Random random = new Random();
  int problemNum = random.nextInt(3);
  int block_kind[] = new int[7];
  Problem(){
    FileInputStream f = null;
    try{
      f = new FileInputStream("problem/"+problemNum);
      Scanner sc = new Scanner(f);
      for(int i=0;i<7;i++){
        block_kind[i] = sc.nextInt();
      }
      f.close();
    }catch(FileNotFoundException e){
      System.err.println("not found");
      System.exit(1);
    }catch(Exception e){
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
  void remove(int kind){
    block_kind[kind] = block_kind[kind]-1;
  }
  void add(int kind){
    block_kind[kind] = block_kind[kind]+1;
  }

  boolean status(int kind){
    if(block_kind[kind] != 0){
      return true;
    }

    return false;
  }

  int getCount(int kind){
    return block_kind[kind];
  }

}

class Block{
    //ブロック定義
    //block[ブロック種類][回転][y座標][x座標]
    int block[][][][] = {
                  {  {  {0,0,0,0},
                      {0,1,0,0},
                      {1,1,1,0},
                      {0,0,0,0}  },

                    {   {0,1,0,0},
                        {0,1,1,0},
                      {0,1,0,0},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                      {1,1,1,0},
                        {0,1,0,0},
                        {0,0,0,0}  },

                    {  {0,0,1,0},
                        {0,1,1,0},
                        {0,0,1,0},
                        {0,0,0,0}  }
                  },
                  {  {  {0,0,0,0},
                      {1,1,1,1},
                      {0,0,0,0},
                      {0,0,0,0}  },

                    {   {0,1,0,0},
                        {0,1,0,0},
                      {0,1,0,0},
                        {0,1,0,0}  },

                    {  {0,0,0,0},
                      {1,1,1,1},
                        {0,0,0,0},
                        {0,0,0,0}  },

                    {  {0,0,1,0},
                        {0,0,1,0},
                        {0,0,1,0},
                        {0,0,1,0}  }
                  },
                  {  {  {0,0,0,0},
                      {0,1,1,0},
                      {0,1,1,0},
                      {0,0,0,0}  },

                    {   {0,0,0,0},
                        {0,1,1,0},
                      {0,1,1,0},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                      {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                        {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}  }
                  },
                  {  {  {0,0,0,0},
                      {1,1,1,0},
                      {0,0,1,0},
                      {0,0,0,0}  },

                    {   {0,0,1,0},
                        {0,0,1,0},
                      {0,1,1,0},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                      {0,1,0,0},
                        {0,1,1,1},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                        {0,1,1,0},
                        {0,1,0,0},
                        {0,1,0,0}  }
                  },
                  {  {  {0,0,0,0},
                      {0,1,1,1},
                      {0,1,0,0},
                      {0,0,0,0}  },

                    {   {0,0,0,0},
                        {0,1,1,0},
                      {0,0,1,0},
                        {0,0,1,0}  },

                    {  {0,0,0,0},
                      {0,0,1,0},
                        {1,1,1,0},
                        {0,0,0,0}  },

                    {  {0,1,0,0},
                        {0,1,0,0},
                        {0,1,1,0},
                        {0,0,0,0}  }
                  },
                  {  {  {0,0,0,0},
                      {1,1,0,0},
                      {0,1,1,0},
                      {0,0,0,0}  },

                    {   {0,0,1,0},
                        {0,1,1,0},
                      {0,1,0,0},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                      {0,1,1,0},
                        {0,0,1,1},
                        {0,0,0,0}  },

                    {  {0,0,0,0},
                        {0,1,0,0},
                        {0,1,1,0},
                        {0,0,1,0}  }
                  },
                  {  {  {0,0,0,0},
                      {0,0,1,1},
                      {0,1,1,0},
                      {0,0,0,0}  },

                    {   {0,0,0,0},
                        {0,1,0,0},
                      {0,1,1,0},
                        {0,0,1,0}  },

                    {  {0,0,0,0},
                      {0,1,1,0},
                        {1,1,0,0},
                        {0,0,0,0}  },

                    {  {0,1,0,0},
                        {0,1,1,0},
                        {0,0,1,0},
                        {0,0,0,0}  }
                  }
                };
    //ブロック基準点の定義
    //blockStart[x座標][y座標]
    int blockStart[][][] = {
                    
                      { {1,1},
                        {1,0},
                        {0,1},
                        {2,0} },

                      { {0,1},
                        {1,0},
                        {0,1},
                        {2,0} },

                      { {1,1},
                        {1,1},
                        {1,1},
                        {1,1} },

                      { {0,1},
                        {2,0},
                        {1,1},
                         {1,1} },

                      { {1,1},
                        {1,1},
                        {2,1},
                        {1,0} },

                      { {0,1},
                        {2,0},
                        {1,1},
                        {1,1} },

                      { {2,1},
                        {1,1},
                        {1,1},
                        {1,0} }
                      
                    };

  int[][] getBlock(int kind,int turn){
    //指定されたブロックを返す
    return block[kind][turn];
  }
}

