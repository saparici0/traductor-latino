public class TraductorPythonVisitor extends LatinoBaseVisitor{
    @Override
    public Object visitSi(LatinoParser.SiContext ctx) {
        System.out.print("if ");
        visitExp(ctx.exp());
        System.out.println(": \n   ");
        visitSecnovac(ctx.secnovac());
        for(int i = 0; i < ctx.osi().toArray().length; i++) {
            visitOsi(ctx.osi(i));
        }

        return 0;
    }

    @Override
    public Object visitExp(LatinoParser.ExpContext ctx) {
        if (ctx.OPI() != null) {
            System.out.print(ctx.OPI().getText());
            visitExp(ctx.exp(0));
        } else if (ctx.val() != null) {
            visitVal(ctx.val());
            for (int i = 0; i < ctx.OP().toArray().length; i++) {
                System.out.print(ctx.OP(i).getText());
                visitExp(ctx.exp(i));
            }
        } else {
            System.out.print("(");
            visitExp(ctx.exp(0));
            System.out.print(")");
            for (int i = 0; i < ctx.OP().toArray().length; i++) {
                System.out.print(ctx.OP(i).getText());
                visitExp(ctx.exp(i));
            }
        }

        return 0;
    }

    @Override
    public Object visitVal(LatinoParser.ValContext ctx) {
        System.out.print(ctx.getText());
        return super.visitVal(ctx);
    }

    @Override
    public Object visitIncr(LatinoParser.IncrContext ctx) {
        if (ctx.INCR().getText().equals("++")) {
            System.out.println(ctx.ID() + " += 1");
        } else {
            System.out.println(ctx.ID() + " -= 1");
        }
        return 0;
    }

    @Override
    public Object visitDesde(LatinoParser.DesdeContext ctx) {
//        if(ctx.asig(0).ID().toArray().length > 1) {
//            print
//        }
        System.out.print("for ");
        System.out.print(ctx.asig(0).ID(0).getText());
        System.out.print(" in range(");
        System.out.print(ctx.asig(0).exp(0).getText());
        System.out.print(",");
        if (ctx.exp().OP(0).getText().equals("<=")){
            int nombre = Integer.parseInt(ctx.exp().exp(0).getText()) + 1;
            System.out.print(nombre);
        }
        else {
            System.out.print(ctx.exp().exp(0).getText());
        }
        System.out.print("):");
        visitSec(ctx.sec());

        return 0;
    }

    public Object visitPara(LatinoParser.ParaContext ctx) {
//        if(ctx.asig(0).ID().toArray().length > 1) {
//            print
//        }
        System.out.print("for ");
        System.out.print(ctx.ID().getText());
        System.out.print(" in range(");
        System.out.print(ctx.paraargs().REAL(0).getText());
        if(ctx.paraargs().REAL().toArray().length > 1){
            for(int i = 1; i < ctx.paraargs().REAL().toArray().length; i++) {
                System.out.print(",");
                System.out.print(ctx.paraargs().REAL(i).getText());
            }
        }
        System.out.print("): \n");
        visitSec(ctx.sec());
        return 0;
    }
}
