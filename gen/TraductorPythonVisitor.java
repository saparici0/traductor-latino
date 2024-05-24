public class TraductorPythonVisitor extends LatinoBaseVisitor{
    private int nivelIdent = 0;

    @Override
    public Object visitAsig(LatinoParser.AsigContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + ctx.ID(0).getText());
        int nvars = ctx.ID().size();
        if (nvars > 1) {
            for (int i = 1; i < nvars; i++) System.out.print(", " + ctx.ID(i).getText());
        }
        System.out.print(" " + ctx.OP_ASIG().getText() + " ");
        

        return super.visitAsig(ctx);
    }

    @Override
    public Object visitSi(LatinoParser.SiContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "if ");
        visitExp(ctx.exp());
        System.out.println(":");

        nivelIdent++;
        visitSecnovac(ctx.secnovac());
        nivelIdent--;

        for(int i = 0; i < ctx.osi().size(); i++) visitOsi(ctx.osi(i));

        if (ctx.sino() != null) visitSino(ctx.sino());

        return 0;
    }

    @Override
    public Object visitOsi(LatinoParser.OsiContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "elif ");
        visitExp(ctx.exp());
        System.out.println(":");
        nivelIdent++;
        visitSecnovac(ctx.secnovac());
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitSino(LatinoParser.SinoContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "else :");
        nivelIdent++;
        visitSecnovac(ctx.secnovac());
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitExp(LatinoParser.ExpContext ctx) {
        if (ctx.OPI() != null) {
            System.out.print(ctx.OPI().getText());
            visitExp(ctx.exp(0));
        } else if (ctx.val() != null) {
            visitVal(ctx.val());
            for (int i = 0; i < ctx.OP().size(); i++) {
                System.out.print(" " + ctx.OP(i).getText() + " ");
                visitExp(ctx.exp(i));
            }
        } else {
            System.out.print("(");
            visitExp(ctx.exp(0));
            System.out.print(")");
            for (int i = 0; i < ctx.OP().size(); i++) {
                System.out.print(ctx.OP(i).getText());
                visitExp(ctx.exp(i));
            }
        }
        return 0;
    }

    @Override
    public Object visitExpv(LatinoParser.ExpvContext ctx) {
        if(ctx.CADENA() != null){
            System.out.print(ctx.CADENA().getText());
        }
        else if(ctx.exp() != null){
            visitExp(ctx.exp(0));
            System.out.print(ctx.OP());
            visitExp(ctx.exp(1));
        }
        else{
            System.out.print("(");
            visitExpv(ctx.expv(0));
            System.out.print(")");
        }
        return 0;
    }

    @Override
    public Object visitFunc(LatinoParser.FuncContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "def " + ctx.ID().getText());
        visitArgs(ctx.args());
        System.out.println(":");
        nivelIdent++;
        visitSecnovac(ctx.secnovac());
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitArgs(LatinoParser.ArgsContext ctx) {
        System.out.print("(");
        if (!ctx.exp().isEmpty()) visitExp(ctx.exp(0));
        if (ctx.exp().size() > 1) {
            System.out.print(",");
            for (int i = 1; i < ctx.exp().size(); i++) visitExp(ctx.exp(i));
        }
        System.out.print(")");
        return 0;
    }

    @Override
    public Object visitVal(LatinoParser.ValContext ctx) {
        if (ctx.ID() != null) {
            System.out.print(ctx.ID().getText());
            if (!ctx.dictargs().isEmpty()) visitDictargs(ctx.dictargs());
        } else if (ctx.llamfunc() != null) {
            visitLlamfunc(ctx.llamfunc());
        } else if (ctx.funcreservret() != null) {
            visitFuncreservret(ctx.funcreservret());
        } else if (ctx.list() != null) {
            visitList(ctx.list());
        } else if (ctx.dic() != null) {
            visitDic(ctx.dic());
        } else {
            if (ctx.REAL() != null) System.out.print(ctx.REAL().getText());
            else if (ctx.BOOL() != null) System.out.print(ctx.BOOL().getText().equals("falso") ? "False" : "True");
            else if (ctx.CADENA() != null) System.out.print(ctx.CADENA().getText());
            else if (ctx.NULO() != null) System.out.print("None");
        }
        return 0;
    }

    @Override
    public Object visitDictargs(LatinoParser.DictargsContext ctx) {
        if (ctx.ID() != null) {
            System.out.print('[');
            System.out.print(ctx.ID().getText());
            System.out.print(']');
            visitDictargs(ctx.dictargs());
        } else if (ctx.expv() != null) {
            System.out.print('[');
            visitExpv(ctx.expv());
            System.out.print(']');
        }
        return 0;
    }

    @Override
    public Object visitSecnovac(LatinoParser.SecnovacContext ctx) {
        visitChildren(ctx);
        return 0;
    }

    @Override
    public Object visitLlamfunc(LatinoParser.LlamfuncContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4)+ctx.ID().getText());
        if(ctx.dictargs() != null){
            visitDictargs(ctx.dictargs());
        }
        visitArgs(ctx.args());
        return 0;
    }

    @Override
    public Object visitIncr(LatinoParser.IncrContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + ctx.ID());
        if (ctx.INCR().getText().equals("++")) {
            System.out.println(" += 1");
        } else {
            System.out.println(" -= 1");
        }
        return 0;
    }

    @Override
    public Object visitDesde(LatinoParser.DesdeContext ctx) {
//        if(ctx.asig(0).ID().size() > 1) {
//            print
//        }
        System.out.print(" ".repeat(nivelIdent * 4) + ctx.asig(0).getText());

        System.out.print("\n" + " ".repeat(nivelIdent * 4) + "while ");
        System.out.print(ctx.exp().getText());
        System.out.println(" :");
        nivelIdent++;
        visitSec(ctx.sec());
        if(ctx.asig().size() > 1) {
            visitAsig(ctx.asig(1));
        }
        else if (ctx.incr() != null){
            visitIncr(ctx.incr());
        }
        else{
            visitLlamfunc(ctx.llamfunc());
        }
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitPara(LatinoParser.ParaContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "for ");
        System.out.print(ctx.ID().getText());
        System.out.print(" in range");
        System.out.print(ctx.paraargs().getText());
        System.out.println(":");
        nivelIdent++;
        visitSec(ctx.sec());
        System.out.print("\n");
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitMientras(LatinoParser.MientrasContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4)+"while ");
        System.out.print(ctx.exp().getText());
        System.out.println(" :");
        nivelIdent++;
        visitSec(ctx.sec());
        System.out.print("\n");
        nivelIdent--;
        return 0;
    }
}
