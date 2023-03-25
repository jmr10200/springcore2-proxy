package hello.proxy.reflection.jdkproxy;

public class CommandBImpl implements CommandB {
    @Override
    public int execute(String command) {
        if ("EAST".equals(command)) {
            return 2;
        } else if ("WEST".equals(command)) {
            return 3;
        } else {
            throw new IllegalArgumentException("Arg must be 'EAST' or 'WEST'");
        }
    }
}
