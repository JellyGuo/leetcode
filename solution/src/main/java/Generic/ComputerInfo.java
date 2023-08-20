package Generic;

public class ComputerInfo extends ESportInfo<ComputerInfo.ComputerEntity>{
    //{"computerInfo":{"ram":1,"cpu":2}}
    public static class ComputerEntity{
        private Computer computerInfo;

        public Computer getComputerInfo() {
            return computerInfo;
        }

        public void setComputerInfo(Computer computerInfo) {
            this.computerInfo = computerInfo;
        }
    }

    public static class Computer{
        private String ram;
        private String cpu;

        public String getRam() {
            return ram;
        }

        public void setRam(String ram) {
            this.ram = ram;
        }

        public String getCpu() {
            return cpu;
        }

        public void setCpu(String cpu) {
            this.cpu = cpu;
        }
    }
}
