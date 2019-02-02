package com.support.calculations;


public class Constant extends CalcItem {

    String type;

    public Constant (String type) {
        this.type = type;
        if (type.equals("PI") ){//PI
            this.pval = new ParserVal( ComplexMath.PI );
        } else if (type.equals("e") ){//Euler's Number
            this.pval = new ParserVal( ComplexMath.E );
        }
    }

    public boolean isConstant () {
        return true;
    }

    public int getType () {
        return 1;
    }

    public CalcItem copy () {
        return new Constant (type);
    }
    
}