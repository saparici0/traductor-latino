public class TraductorPythonVisitor extends LatinoBaseVisitor{
    private int nivelIdent = 0;

    @Override
    public Object visitSec (LatinoParser.SecContext ctx) {
        super.visitSec(ctx);
        return 0;
    }

    @Override
    public Object visitAsig(LatinoParser.AsigContext ctx) {
        int k = 0;
        for(int j = 0; j < ctx.asigadc().size(); j++){
            if(ctx.asigadc(j).funcalt() != null){
                k += 1;
            }
        }

        if (ctx.funcalt() != null || k != 0) {
            if (ctx.funcalt() != null) {
                System.out.print(" ".repeat(nivelIdent * 4) + "def " + ctx.ID(0).getText());
                visitArgs(ctx.funcalt().args());
                System.out.print(" :");
                nivelIdent++;
                visitSecnovac(ctx.funcalt().secnovac());
                nivelIdent--;
                System.out.println();
            } else {
                System.out.print(" ".repeat(nivelIdent * 4) + ctx.ID(0).getText());
                System.out.print(" " + ctx.OP_ASIG().getText() + " ");
                visit(ctx.exp());
                System.out.println();
            }

            for (int i = 0; i < ctx.asigadc().size(); i++) {
                if (ctx.asigadc(i).funcalt() != null) {
                    System.out.print(" ".repeat(nivelIdent * 4) + "def " + ctx.ID(i+1).getText());
                    visitArgs(ctx.asigadc(i).funcalt().args());
                    System.out.print(" :");
                    nivelIdent++;
                    visitSecnovac(ctx.asigadc(i).funcalt().secnovac());
                    nivelIdent--;
                    System.out.println();
                }
                else{
                    System.out.print(" ".repeat(nivelIdent * 4) + ctx.ID(i+1).getText());
                    System.out.print(" " + ctx.OP_ASIG().getText() + " ");
                    visit(ctx.asigadc(i).exp());
                    System.out.println();
                }
            }
        }
        else {
            System.out.print(" ".repeat(nivelIdent * 4) + ctx.ID(0).getText());
            int nvars = ctx.ID().size();
            if (nvars > 1) {
                for (int i = 1; i < nvars; i++) System.out.print(", " + ctx.ID(i).getText());
            }
            System.out.print(" " + ctx.OP_ASIG().getText() + " ");
            if (ctx.exp() != null) {
                visitExp(ctx.exp());
            }
            for (int i = 0; i < ctx.asigadc().size(); i++){
                visitExp(ctx.asigadc(i).exp());
            }
        }
        return 0;
    }

    @Override
    public Object visitAsigadc(LatinoParser.AsigadcContext ctx) {
        if(ctx.exp() != null){
            System.out.print(", ");
            visitExp(ctx.exp());
        }
        return 0;
    }

    @Override
    public Object visitSi(LatinoParser.SiContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "if ");
        visitExp(ctx.exp());
        System.out.print(":");

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
        System.out.print(":");
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
        if (ctx.NOT() != null) {
            System.out.print(ctx.NOT().getText());
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
    public Object visitValllave(LatinoParser.ValllaveContext ctx) {
        if (ctx.ID() != null) {
            System.out.print(ctx.ID().getText());
            if (!ctx.dictargs().isEmpty()) visitDictargs(ctx.dictargs());
        } else if (ctx.llamfunc() != null) {
            visitLlamfunc(ctx.llamfunc());
        } else if (ctx.funcreservret() != null) {
            visitFuncreservret(ctx.funcreservret());
        } else {
            if (ctx.REAL() != null) System.out.print(ctx.REAL().getText());
            else if (ctx.BOOL() != null) System.out.print(ctx.BOOL().getText().equals("falso") ? "False" : "True");
            else if (ctx.CADENA() != null) System.out.print(ctx.CADENA().getText());
            else if (ctx.NULO() != null) System.out.print("None");
        }
        return 0;
    }

    @Override
    public Object visitExpllave(LatinoParser.ExpllaveContext ctx) {
        if (ctx.valllave() != null){
            visitValllave(ctx.valllave());
        } else if (!ctx.exp().isEmpty()){
            visitExp(ctx.exp(0));
            System.out.print(ctx.OP().getText());
            visitExp(ctx.exp(1));
        } else {
            System.out.print("(");
            visitExpllave(ctx.expllave(0));
            System.out.print(")");
        }
        return 0;
    }

    @Override
    public Object visitFunc(LatinoParser.FuncContext ctx) {
        System.out.print("\n"+" ".repeat(nivelIdent * 4) + "def " + ctx.ID().getText());
        visitArgs(ctx.args());
        System.out.print(":");
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
            System.out.print("[");
            System.out.print(ctx.ID().getText());
            System.out.print("]");
            visitDictargs(ctx.dictargs());
        } else if (ctx.expllave() != null) {
            System.out.print("[");
            visitExpllave(ctx.expllave());
            System.out.print("]");
        }
        return 0;
    }

    @Override
    public Object visitSecnovac(LatinoParser.SecnovacContext ctx) {
        System.out.println();
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
            System.out.print(" += 1");
        } else {
            System.out.print(" -= 1");
        }
        return 0;
    }

    @Override
    public Object visitDesde(LatinoParser.DesdeContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + ctx.asig(0).getText());

        System.out.print("\n" + " ".repeat(nivelIdent * 4) + "while ");
        System.out.print(ctx.exp().getText());
        System.out.print(" :");
        nivelIdent++;
        visitSec(ctx.sec());
        System.out.println();
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
        System.out.print(":");
        nivelIdent++;
        visitSec(ctx.sec());
        System.out.print("\n");
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitMientras(LatinoParser.MientrasContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "while ");
        System.out.print(ctx.exp().getText());
        System.out.print(" :");
        nivelIdent++;
        visitSec(ctx.sec());
        System.out.print("\n");
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitFuncalt(LatinoParser.FuncaltContext ctx) {
        return super.visitFuncalt(ctx);
    }

    @Override
    public Object visitFuncreserv(LatinoParser.FuncreservContext ctx) {
        if(ctx.FUNCRESERV1() != null){
            if (ctx.FUNCRESERV1().getText().equals("escribir") || ctx.FUNCRESERV1().getText().equals("imprimir") || ctx.FUNCRESERV1().getText().equals("poner")){
                System.out.print(" ".repeat(nivelIdent * 4)+"print(");
            }
            else{
                System.out.print(" ".repeat(nivelIdent * 4)+"raise ValueError(");
            }

            visitExp(ctx.exp(0));
            System.out.print(")");
        }
        else if(ctx.IMPRIMIRF() != null){
            System.out.print(" ".repeat(nivelIdent * 4)+"print(");
            System.out.print(ctx.CADENA().getText());
            if(ctx.exp() != null){
                for(int i = 0; i < ctx.exp().size(); i++){
                    if(i==0){
                        if(i == ctx.exp().size()-1){
                            System.out.print(" % ("+ctx.exp(i).getText()+"))");
                        }
                        else {
                            System.out.print(" % (" + ctx.exp(i).getText());
                        }
                    }
                    else if(i==ctx.exp().size()-1){
                        System.out.print(", "+ctx.exp(i).getText()+"))");
                    }
                    else{
                        System.out.print(", " + ctx.exp(i).getText());
                    }
                }
            }
        }

        else {
            System.out.print(" ".repeat(nivelIdent * 4)+"os.system('clear')");
        }

        return 0;
    }

    @Override
    public Object visitFuncreservret(LatinoParser.FuncreservretContext ctx) {
        if(ctx.FUNCRESERVRET() != null){
            if(ctx.FUNCRESERVRET().getText().equals("tipo")){
                System.out.print("type");
                visitArgs(ctx.args());
            } else if(ctx.FUNCRESERVRET().getText().equals("acadena")){
                System.out.print("str");
                visitArgs(ctx.args());
            } else if (ctx.FUNCRESERVRET().getText().equals("alogico")) {
                System.out.print("bool");
                visitArgs(ctx.args());
            } else if (ctx.FUNCRESERVRET().getText().equals("anumero")) {
                System.out.print("float");
                visitArgs(ctx.args());
            }
            else{//fct leer()
                System.out.print("leer()");
            }
        }
        return 0;
    }

    @Override
    public Object visitRetornar(LatinoParser.RetornarContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "return ");
        visitExp(ctx.exp());
        return 0;
    }

    @Override
    public Object visitList(LatinoParser.ListContext ctx) {
        if (ctx.exp() != null){
            System.out.print("[");
            visitExp(ctx.exp(0));
            if (ctx.exp().size() > 1) {
                for (int i = 1; i < ctx.exp().size(); i++) {
                    System.out.print(",");
                    visitExp(ctx.exp(i));
                }
            }
            System.out.print("]");
        } else {
            System.out.print("[]");
        }
        return 0;
    }

    @Override
    public Object visitRomper(LatinoParser.RomperContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4)+"break");
        return 0;
    }

    @Override
    public Object visitRepetir(LatinoParser.RepetirContext ctx){
        System.out.print(" ".repeat(nivelIdent * 4) + "while ");
        System.out.print(ctx.exp().getText());
        System.out.print(" :");
        nivelIdent++;
        visitSec(ctx.sec());
        nivelIdent--;
        return 0;
    }

    @Override
    public Object visitElegir(LatinoParser.ElegirContext ctx){
        System.out.print(" ".repeat(nivelIdent * 4) + "if ");
        System.out.print(ctx.exp().getText() + " == " + ctx.caso(0).exp().getText() + ":");
        nivelIdent++;
        visitSec(ctx.caso(0).sec());
        System.out.println();
        nivelIdent--;
        for(int i = 1; i < ctx.caso().size(); i++) {
            System.out.print(" ".repeat(nivelIdent * 4) + "elif ");
            System.out.print(ctx.exp().getText() + " == " + ctx.caso(i).exp().getText() + ":");
            nivelIdent++;
            visitSec(ctx.caso(i).sec());
            System.out.println();
            nivelIdent--;
        }
        if(ctx.otro() != null){
            System.out.print(" ".repeat(nivelIdent * 4) + "else: ");
            nivelIdent++;
            visitSec(ctx.otro().sec());
            System.out.println();
            nivelIdent--;
        }

        return 0;
    }
}
