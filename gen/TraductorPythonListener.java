import java.io.File;

public class TraductorPythonListener extends LatinoBaseListener {
    File file = new File("../file.py");

    @Override
    public void enterGram(LatinoParser.GramContext ctx) {
        System.out.println("gram " + ctx.getText());
    }

    @Override
    public void enterAsig(LatinoParser.AsigContext ctx) {
        System.out.println(ctx.ID());
        for (int i = 0; i < ctx.ID().size(); i++) {
            System.out.println("var " + ctx.ID(i).getText());
        }
    }

    @Override
    public void enterRomper(LatinoParser.RomperContext ctx) {
        System.out.println(ctx.getText());
    }

    @Override
    public void enterIncr(LatinoParser.IncrContext ctx) {
        if (ctx.INCR().getText().equals("++")) {
            System.out.println(ctx.ID() + " += 1");
        } else {
            System.out.println(ctx.ID() + " -= 1");
        }
    }

    @Override
    public void enterSi(LatinoParser.SiContext ctx) {
        System.out.println("if " + ctx.exp().getText() + ":");
    }

}