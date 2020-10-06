package algorithms;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.nio.file.Files.list;
import java.util.Collections;
import java.util.ArrayDeque;
import java.io.*;
import java.net.*;
import java.util.*;



/**
 * @author Αναστασία Κασσιανή Μπλίτση
 */
public class Mines {

    public static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        /**
         * Prints the point
         */
        public void displayPoint() {
            System.out.println(this.x + " " + this.y);
        }

        private void push_back(int dest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }


    public ArrayList<Point> quickHull(ArrayList<Point> points) {
        ArrayList<Point> convexHull = new ArrayList<>();
        if (points.size() < 3)
            return (ArrayList) points.clone();

        int minPoint = -1, maxPoint = -1;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x < minX) {
                minX = points.get(i).x;
                minPoint = i;
            }
            if (points.get(i).x > maxX) {
                maxX = points.get(i).x;
                maxPoint = i;
            }
        }
        Point A = points.get(minPoint);
        Point B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<Point> leftSet = new ArrayList<>();
        ArrayList<Point> rightSet = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);

        return convexHull;
    }

    public int distance(Point A, Point B, Point C) {
        int ABx = B.x - A.x;
        int ABy = B.y - A.y;
        int num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
        if (num < 0)
            num = -num;
        return num;
    }


    public void hullSet(Point A, Point B, ArrayList<Point> set,
                        ArrayList<Point> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1) {
            Point p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        int dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++) {
            Point p = set.get(i);
            int distance = distance(A, B, p);
            if (distance > dist) {
                dist = distance;
                furthestPoint = i;
            }
        }
        Point P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);

        // Determine who's to the left of AP
        ArrayList<Point> leftSetAP = new ArrayList<Point>();
        for (int i = 0; i < set.size(); i++) {
            Point M = set.get(i);
            if (pointLocation(A, P, M) == 1) {
                leftSetAP.add(M);
            }
        }

        // Determine who's to the left of PB
        ArrayList<Point> leftSetPB = new ArrayList<Point>();
        for (int i = 0; i < set.size(); i++) {
            Point M = set.get(i);
            if (pointLocation(P, B, M) == 1) {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }

    public int pointLocation(Point A, Point B, Point P) {
        int cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }



     /**
     * Here is just the type for the calculation above.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double calculateD(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    /**
     * Here i calculate path distance.
     * i1 and i2 are the spots and sum is the total distance.
     *
     * @param path
     * @return
     */
    public double pathDistance(ArrayList<Point> path) {
        double sum = 0;
        int i = 0, j = 1;
        while (j < path.size()) {
            sum += calculateD(path.get(i).getX(), path.get(i).getY(), path.get(j).getX(), path.get(j).getY());
            i++;
            j++;
        }
        return sum;
    }

    

    /**
     
     * @param polygon
     * @param t     index of tresure point in convex hull
     * @return shortest path
     
     */
public ArrayList<Point> paths(int t, ArrayList<Point> polygon) {

        ArrayList<Point> path1 = new ArrayList<Point>();
        ArrayList<Point> path2 = new ArrayList<Point>();

        
        for (int i = t; i < polygon.size(); i++) {   //half path2
            if(i<=t)
                path1.add(polygon.get(i));
            else
                path2.add(polygon.get(i));
        }
        
       Collections.reverse(path2);         //reverse path 2,cause it started from the treasure.
       path2.add(polygon.get(t));
       
       if (pathDistance(path1) < pathDistance(path2))
           return path1;
       else
           return path2;
       
    }
    
    
  

    /**
     * print the shortest path.
     *
     * @param path
     */
    public void toStr(ArrayList<Point> path) {
        System.out.print("The shortest path is:");
        for (int i = 0; i < path.size(); i++) {
            Point p = path.get(i);
            System.out.print("(" + (float) p.getX() + "," + (float) p.getY() + ")");
            if (i != path.size() - 1) {
                System.out.print("-->");
            }
        }
        System.out.println();
    }
//Note this is intentially a simple algorithm, many faster options are out there
public int findNearestIndex(Point thisPoint, ArrayList<Point> listToSearch) {
    double nearestDistSquared=Double.POSITIVE_INFINITY;
    int nearestIndex = 0;
    for (int i=0; i< listToSearch.size(); i++) {
        Point point2=listToSearch.get(i);
        int distsq = (thisPoint.x - point2.x)*(thisPoint.x - point2.x) 
               + (thisPoint.y - point2.y)*(thisPoint.y - point2.y);
        if(distsq < nearestDistSquared) {
            nearestDistSquared = distsq;
            nearestIndex=i;
        }
    }
    return nearestIndex;
}
    
   //Puts the points of the convex hull in order  
   public ArrayList<Point> makeOrder(ArrayList<Point> points,Point s, Point d){
       
       ArrayList<Point> orderedList = new ArrayList<Point>();

       orderedList.add(s); //Arbitrary starting point
       points.remove(s);
       while (points.size() > 0) {
   //Find the index of the closest point (using another method)
           int nearestIndex=findNearestIndex(orderedList.get(orderedList.size()-1), points);

   //Remove from the unorderedList and add to the ordered one
          orderedList.add(points.remove(nearestIndex));
      } 
       orderedList.add(s);
       return orderedList; 
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */

    public static void main(String[] args) throws FileNotFoundException, IOException {

         int xs = 0, ys = 0, xt = 0, yt = 0;
        Point e;
        //except of mines it also contains the human and the treaure
        ArrayList<Point> points = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("test1.txt"))) {
            String line = reader.readLine();
            String[] cor = line.split(" ");
            int x = Integer.parseInt(cor[0]);
            int y = Integer.parseInt(cor[1]);
            xs = x;
            ys = y;
            e = new Point(x, y);
            points.add(e);
            line = reader.readLine();
            cor = line.split(" ");
            x = Integer.parseInt(cor[0]);
            y = Integer.parseInt(cor[1]);
            xt = x;
            yt = y;
            e = new Point(x, y);
            points.add(e);
            while (line != null) {
                cor = line.split("");
                x = Integer.parseInt(cor[0]);
                y = Integer.parseInt(cor[1]);
                e = new Point(x, y);
                points.add(e);
                line = reader.readLine();
            }

        } catch (IOException l) {
        }

        Mines qh = new Mines();
        ArrayList<Point> p = qh.quickHull(points);


        int s = 0, t = 0;  //finding the index of start and end in convex hull and.. ..
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).getX() == xs && p.get(i).getY() == ys) {
                s = i;
            }
            if (p.get(i).getX() == xt && p.get(i).getY() == yt) {
                t = i;
            }
        }
        ArrayList<Point> order = qh.makeOrder(p, p.get(s), p.get(t));
        for (int i = 0; i < order.size(); i++) {
            if (order.get(i).getX() == xt && order.get(i).getY() == yt) {
                t = i;
            }
        }
        ArrayList<Point> shortestpath = qh.paths(t, order); //...and sending them to find the 2 different paths leading to the treasure. //...and sending them to find the 2 different paths leading to the treasure.
        
            System.out.println("The shortest distance is " + qh.pathDistance(shortestpath));     //finding the shortest path and printing it.
            qh.toStr(shortestpath);
       
    

    }
}


/**
 *  
 *  System.out.println("Quick Hull Test");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of points");
        int N = sc.nextInt();
 
        ArrayList<Point> points = new ArrayList<Point>();
        System.out.println("Enter the coordinates of each points: <x> <y>");
        for (int i = 0; i < N; i++)
        {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Point e = new Point(x, y);
            points.add(i, e);
        }
 
        Mines qh = new Mines();
        ArrayList<Point> p = qh.quickHull(points);
        System.out
                .println("The points in the Convex hull using Quick Hull are: ");
        for (int i = 0; i < p.size(); i++)
            System.out.println("(" + p.get(i).x + ", " + p.get(i).y + ")");
        sc.close();
 * 
 * 
 * 
 * 
 * 

    public int CounterClockWise(Point a, Point b, Point c) {

        int delta = (int) (((b.getX() - a.getX()) * (c.getY() - a.getY())) - ((b.getY() - a.getY()) * (c.getX() - a.getX())));

        if (delta < 0)       // clockwise
            return -1;
        if (delta > 0)       // counterclockwise
            return 1;
        else                // point a,b,c are collinear
            return 0;
    }
 * 
 * 
 * 
 * 
 * 
    public void findPeriphery() {
        ConvexHull.push(points[0]);
        ConvexHull.push(points[1]);     // these two points are definately on the hull

        for (int i = 2; i < No_of_points; ++i) {

            System.out.println(i);
            Point top = ConvexHull.pop();

            while (CounterClockWise(ConvexHull.peek(), top, points[i]) == -1) {  // discarding points that would
                top = ConvexHull.pop();                                          // create clockwise turn
                //dispStackContents();
            }

            ConvexHull.push(top);
            ConvexHull.push(points[i]);
            dispStackContents();
        }
    }


 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * BufferedReader reader;
 * ArrayList<Point> points = new ArrayList<Point>();
 * int x,y,num=0;
 * int sumSpace=0, sumTab=0;
 * String a = null;
 * String [] res = null;
 * try {
 * String fileName = "test1.txt";
 * reader = new BufferedReader(new FileReader(fileName));
 * String line = reader.readLine();
 * num = Integer.parseInt(line);
 * line = reader.readLine();
 * for(int i = 0; i<line.length(); i++) {
 * if (line.charAt(i) == ' ')
 * sumSpace++;
 * else if (line.charAt(i) == '\t')
 * sumTab++;
 * }
 * if (sumSpace > sumTab)
 * a = " ";
 * else if (sumTab > sumSpace)
 * a = "\t";
 * while (line != null) {
 * res = line.split(a);
 * x = Integer.parseInt(res[0]);
 * y = Integer.parseInt(res[1]);
 * Point e = new Point(x, y);
 * points.add(e);
 * line = reader.readLine();
 * }
 * reader.close();
 * <p>
 * } catch (IOException e) {
 * }
 * Mines qh = new Mines();
 * ArrayList<Point> p = qh.quickHull(points);
 * for (int i=0;i<p.size();i++) {
 * p.get(i).displayPoint();
 * }
 */


