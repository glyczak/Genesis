import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BeanGenerator {
    private static List<Variable> variables = new ArrayList<Variable>();
    private String beanName;

    private boolean loaded = false;

    private class Variable {
        private String dataType;
        private String name;

        public String getDataType() {
            return dataType;
        }

        public String getName() {
            return name;
        }

        public String getUpperName() {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        public Variable(String dataType, String name) {
            this.dataType = dataType;
            this.name = name;
        }
    }

    public void load(String beanName) {
        this.beanName = beanName;
        Scanner scanner = new Scanner(System.in);
        String line;
        int breakIndex;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(line.isEmpty()) break;
            breakIndex = line.indexOf(" ");
            variables.add(new Variable(line.substring(0, breakIndex), line.substring(breakIndex + 1)));
        }
        scanner.close();
        this.loaded = true;
    }

    public void generate() {
        if(!this.loaded) return;
        System.out.println("import java.io.Serializable;\n");
        System.out.println(String.format("public class %s implements Serializable {", this.beanName));
        for (Variable v : variables) {
            System.out.println(String.format("    private %s %s;", v.getDataType(), v.getName()));
        }
        for (Variable v : variables) {
            System.out.println(String.format("\n    public %s get%s() {", v.getDataType(), v.getUpperName()));
            System.out.println(String.format("        return this.%s;", v.getName()));
            System.out.println("    }");
            System.out.println(
                    String.format("\n    public void set%s(%s %s) {", v.getUpperName(), v.getDataType(), v.getName()));
            System.out.println(String.format("        this.%s = %s;", v.getName(), v.getName()));
            System.out.println("    }");
        }
        System.out.println("}");
        this.variables.clear();
        this.beanName = null;
        this.loaded = false;
    }

    public static void main(String args[]) {
        BeanGenerator b = new BeanGenerator();
        b.load(args[0]);
        b.generate();
    }
}
