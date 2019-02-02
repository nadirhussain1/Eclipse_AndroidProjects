package com.support.calculations;


public class ComplexNumber extends CalcItem {

    public static final double DELTA_POS=0.000000000001;
    public static final double DELTA_NEG=-0.000000000001;

    public double re,im;

    public ComplexNumber(double re, double im){
        this.re = re;
        this.im = im;
        pval = new ParserVal (this);
    }

    public CalcItem copy () {
        return new ComplexNumber (re, im);
    }

    public int getType () {
        return 1;
    }

    public boolean isNum () {
        return true;
    }

    static ComplexNumber newCartesian(double re, double im){
        return new ComplexNumber(re,im);
    }

    static ComplexNumber newPolar(double R, double theta){
        return new ComplexNumber(R*Math.cos(theta) , R*Math.sin(theta) );
    }

    public void negate () {
        this.re *= -1.0;
        this.im *= -1.0;
    }

    public double Re(){
        return this.re;
    }
    
    public double Im(){
        return this.im;
    }

    public void setRe (Double val) {
        this.re = val;
    }

    public void setIm (Double val) {
        this.im = val;
    }

    public double R(){
        return Math.sqrt(Math.pow(re,2) + Math.pow(im,2) );
    }

    double Theta(){
        return Math.atan2(this.im , this.re);
    }

    ComplexNumber conjugate () {
        return newCartesian(this.re, - this.im );
    }
   
    public String toString () {
        String result;
        if (im == 0) result = Double.toString(re);
        else if (re == 0) result = Double.toString(im) + "i";
        else result = re + "+" + im + "i";
        if ( Double.isInfinite(this.Re() ) || Double.isInfinite(this.Im() ) )
            result = "INFINITY";
        if ( Double.isNaN(this.Re() ) || Double.isNaN(this.Im() ) )
            result = "NaN";

        return result;
    }

    public ComplexNumber round (int places) {
        if ( Double.isInfinite(this.Re() ) || Double.isInfinite(this.Im() ) )
            return this;
        if ( Double.isNaN(this.Re() ) || Double.isNaN(this.Im() ) )
            return this;
        return newCartesian(this.round(this.re, places), this.round(this.im, places) );
    }

    /*
    public static double round (double d, int places) {
        double rounder = Math.pow(10, places);
        double result = Math.round(d * rounder);
        return (result/rounder);
    }
    */

    public static String roundLast (String s, char c) {
        if (s.charAt (s.length()-1) == '.') {
            String num = s.substring (0, s.indexOf ('.'));
            return roundLast (num, c) + s.substring (s.indexOf ('.'), s.length ());
        } else if (c < '5')
            return s;
        else {
            String r = s.substring (0,s.length()-1);
            char old = s.charAt (s.length()-1);
            if (old == '9')
                return roundLast (r, c)+"0";
            else {
                return r + Character.toString ((char)(old + 1));
            }
        }
    }

    public static double clean(double x) {
	if(x < DELTA_POS && x > DELTA_NEG) return 0;
	return x;
    }

    public ComplexNumber clean() {
	if(re < DELTA_POS && re > DELTA_NEG) {
	    re = 0;
	}
	if(im < DELTA_POS && im > DELTA_NEG) {
	    im = 0;
	}
	return this;
    }

    public static double round (double d, int places) {
        return Double.valueOf(roundStr(d, places));
    }

    public static String roundStr (double d, int places) {
        String str = Double.toString (d), end;
        int dec = str.indexOf ('.'), eloc = str.indexOf ('E'), last = str.length ();
        String start = str.substring (0, dec+1);
        if (eloc == -1) {
            if (dec+places+1 < last) {
                last = dec+places+1;
                end = roundLast (str.substring (0,last), str.charAt (last));
            } else {
                end = str;
            }
        } else {
            String middle = str.substring (dec+1, eloc);
            if (middle.length () < places) {
                end = str;
            } else {
                end = roundLast (str.substring (0,dec+places), str.charAt (dec+places))
                    + str.substring (eloc);
            }
        }
        return end;
    }

    public double arg() {
	return Math.atan2(im+DELTA_POS/1000.0, re);
    }

    public void Add(ComplexNumber z2){
        this.re += z2.re;
        this.im += z2.im;
    }

    public void Sub(ComplexNumber z2){
        this.re -= z2.re;
        this.im -= z2.im;
    }
    
    public void Mult(ComplexNumber z2){
        double R = this.R()*z2.R();
        double theta = this.Theta() + z2.Theta();
        this.re = R*Math.cos(theta);
        this.im = R*Math.sin(theta);
    }

    public void Div(ComplexNumber z2){
        if ( ComplexMath.Equal(z2,ComplexMath.Zero) ) {
            this.re = Double.POSITIVE_INFINITY;
            this.im = Double.POSITIVE_INFINITY;
        } else {
            double R = this.R()/z2.R();
            double theta = this.Theta() - z2.Theta();
            this.re = R*Math.cos(theta);
            this.im = R*Math.sin(theta);
        }
    }
    
    public void Pow (ComplexNumber z2) {
        ComplexNumber tmp1 = ComplexMath.Log(this);
        tmp1.Mult (z2);
        double R = Math.exp(tmp1.re);
        double theta = tmp1.im;
        this.re = R*Math.cos(theta);
        this.im = R*Math.sin(theta);
    }
}
