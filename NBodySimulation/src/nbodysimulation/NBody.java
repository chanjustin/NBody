package nbodysimulation;

/**
 *
 * @author Justin Chan
 * @version 1.0
 */

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import nbody.*;

public class NBody 
{
    private static final double UNIVERSAL_GRAVITATIONAL_FORCE = 6.67E-11;
    private static int numberOfParticles;
    private static double radiusOfUniverse;
    private static double[] xPosition;
    private static double[] yPosition;
    private static double[] xVelocity;
    private static double[] yVelocity;
    private static double[] mass;
    private static String[] image;
    private static double[] xForce;
    private static double[] yForce;
    private static String fileName = "end-entropy-universe";
    
    public static void main(String[] args) throws Exception
    {
        Scanner input = new Scanner(new BufferedReader(new FileReader("src/nbody/" + fileName + ".txt")));
        numberOfParticles = Integer.parseInt(input.next());
        xPosition = new double[numberOfParticles];
        yPosition = new double[numberOfParticles];
        xVelocity = new double[numberOfParticles];
        yVelocity = new double[numberOfParticles];
        mass = new double[numberOfParticles];
        image = new String[numberOfParticles];
        xForce = new double[numberOfParticles];
        yForce = new double[numberOfParticles];
        
        radiusOfUniverse = Double.parseDouble(input.next());
        
        for(int i = 0; i < numberOfParticles; i++)
        {
            xPosition[i] = Double.parseDouble(input.next());
            yPosition[i] = Double.parseDouble(input.next());
            xVelocity[i] = Double.parseDouble(input.next());
            yVelocity[i] = Double.parseDouble(input.next());
            mass[i] = Double.parseDouble(input.next());
            image[i] = input.next();
        }
        
        System.out.println("Number of particles: " + numberOfParticles);
        System.out.println("Radius of universe: " + radiusOfUniverse);

        System.out.printf("\n%11s %11s %11s %11s %11s %12s\n","xPosition","yPosition","xVelocity","yVelocity","mass","image");
        for (int i = 0; i < numberOfParticles; i++) 
        {
            System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                   xPosition[i], yPosition[i], xVelocity[i], yVelocity[i], mass[i], image[i]);
        }   
        StdDraw.setXscale(-radiusOfUniverse, radiusOfUniverse);
        StdDraw.setYscale(-radiusOfUniverse, radiusOfUniverse);
        NBody nbody = new NBody(157788000.0,25000.0);
    }
    
    public NBody(double T, double timeQuantum)
    {   
        int currentTime = 0;
        StdAudio.play("src/nbody/2001.mid");

        while(currentTime < T)
        {   
            for(int i = 0; i < numberOfParticles; i++)
            {
                xForce[i] = 0;
                yForce[i] = 0;
            }
            
            for(int i = 0; i < numberOfParticles; i++)
            {
                for(int j = 0; j < numberOfParticles; j++)
                {
                    if(i==j)
                    {
                        continue;
                    }
                    double xDistance;
                    double yDistance;
                    
                    if(xPosition[j] > xPosition[i])
                    {
                        xDistance = xPosition[j] - xPosition[i];
                    }
                    else if(xPosition[j] == xPosition[i])
                    {
                        xDistance = 0;
                    }
                    else
                    {
                        xDistance = -(xPosition[i]-xPosition[j]);
                    }
                    
                    if(yPosition[j] > yPosition[i])
                    {
                        yDistance = yPosition[j] - yPosition[i];
                    }
                    else if(yPosition[j] == yPosition[i])
                    {
                        yDistance = 0;
                    }
                    else
                    {
                        yDistance = -(yPosition[i]-yPosition[j]);
                    }
                    
                    double distance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
                    
                    double netForce = UNIVERSAL_GRAVITATIONAL_FORCE*(mass[i]*mass[j])/Math.pow(distance,2);
                    
                        xForce[i] += (netForce*xDistance)/distance;
                    
                        yForce[i] += (netForce*yDistance)/distance;
                    
                }
            }
            for(int i = 0; i < numberOfParticles; i++)
            {
                xPosition[i] = xPosition[i]+(timeQuantum*xVelocity[i]);
                yPosition[i] = yPosition[i]+(timeQuantum*yVelocity[i]);
                
                xVelocity[i] = xVelocity[i]+(timeQuantum*(xForce[i]/mass[i]));
                yVelocity[i] = yVelocity[i]+(timeQuantum*(yForce[i]/mass[i]));
            }
            StdDraw.picture(0,0,"starfield.jpg");
            for(int i = 0; i < numberOfParticles; i++)
            {
                StdDraw.picture(xPosition[i],yPosition[i],image[i]);
            }
            StdDraw.show(30);
            currentTime += timeQuantum;
        }
        try
        {
            printToFile();
        }
        catch(IOException e)
        {
            
        }
    }
    
    public void printToFile() throws IOException
    {
        PrintWriter output = new PrintWriter(new FileWriter("src/nbody/end-" + fileName + ".txt"),true);
        output.println(numberOfParticles);
        output.println(radiusOfUniverse);
        for(int i = 0; i < numberOfParticles; i++)
        {
            output.println(xPosition[i] + "\t" + yPosition[i] + "\t" + xVelocity[i] + "\t" + yVelocity[i] + "\t" + mass[i] + "\t" + image[i]);
        }
        output.flush();
    }
}