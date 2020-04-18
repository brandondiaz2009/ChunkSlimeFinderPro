import java.util.*;

public class ChunkSlimes {
        public static void main(String[] args) throws Exception {

        var seedInput = "-862192252229339605"; // Seed del juego

        if ( ! seedInput.matches("^[0-9,-]+$") ) {
                        System.out.println("\"" + seedInput + "\" is NOT a numerical value!\nPlease use a NUMERICAL Minecraft Seed!");
                        return;
                } // Verifica la seed
        long seed = Long.parseLong(seedInput);

        // Funcion espiral para buscar CERCA de nuestra zona

        int x = 0, y = 0; // Corrdenadas iniciales
        int input = 0; // Total de saltos 0 = infinito

        int totalStepsNear = 8; // Default 8 = 3x3
        int totalSlimesNear = 8; // Cantidad de chhunks cerca en los pasos

        int step_count = 1;
        int step_limit = 2;
        int adder = 1;

        for (int n = 2; n != input + 1;n++, step_count++)
        {
            if (step_count <= .5 * step_limit)
                x += adder;
            else if (step_count <= step_limit)
                y += adder;
            if (step_count == step_limit)
            {
                adder *= -1;
                step_limit += 2;
                step_count = 0;
            }

            var checkChunk = slime(seed,x,y);

            if(totalStepsNear == 1){
                    System.out.println("Found " + totalSlimesNear + " chunks near on: X:" + Integer.toString(x * 16) + " Z:" + Integer.toString(y * 16));
                    return;
            }else{
                var slimesNear = checkSlimesNear(seed,x-1,y,totalStepsNear,totalSlimesNear);
                if(slimesNear){
                    System.out.println("Found " + totalSlimesNear + " chunks near on: X:" + Integer.toString(x * 16) + " Z:" + Integer.toString(y * 16));
                    return;
                }
           }

           if(n%1000000==0){
                    System.out.println("Looking near...  X:" + Integer.toString(x * 16) + " Z:" + Integer.toString(y * 16));
           }
        }

    }

    public static boolean slime(long seed, int xPosition, int zPosition){
        Random rnd = new Random(
             seed +
            (int) (xPosition * xPosition * 0x4c1906) +
            (int) (xPosition * 0x5ac0db) +
            (int) (zPosition * zPosition) * 0x4307a7L +
            (int) (zPosition * 0x5f24f) ^ 0x3ad8025f
        );
        return rnd.nextInt(10) == 0;
    }

    public static boolean checkSlimesNear(long seed, int xPosition, int zPosition, int totalStepsNear, int totalSlimeNear){
        // 8 steps = 3 x 3
        // 24 steps = 5 x 5
        // Formula (X * X) -1 = total Steps

        //System.out.println("Checking near... X: " + xPosition + " Y: " + zPosition);
        //System.out.println("Steps: " + totalStepsNear);

        int totalSlimesDetected = 0;
        int stepsDone = 0;
        int coordsFound[][];
        coordsFound = new int[totalStepsNear][2];

        totalStepsNear++;



        int x = xPosition, y = zPosition;
        int input = totalStepsNear;
        int step_count = 1;
        int step_limit = 2;
        int adder = 1;

        for (int n = 2; n != input + 1;n++, step_count++)
        {
            if (step_count <= .5 * step_limit){
                x += adder;
            }else if (step_count <= step_limit){
                y += adder;
            }

            if (step_count == step_limit)
            {
                adder *= -1;
                step_limit += 2;
                step_count = 0;
            }


            var checkChunk = slime(seed,x,y);
            //System.out.println("X: " + x + " Y: " + y);

            if(checkChunk){
                totalSlimesDetected++;
                coordsFound[stepsDone][0] = x;
                coordsFound[stepsDone][1] = y;
            }

            stepsDone++;

            if(totalStepsNear - stepsDone < totalSlimeNear - totalSlimesDetected){
                 return false; //No hay posibilidad de encontrar slimes en este chunk salga al siguiente
            }

        }


           if(totalSlimesDetected>=totalSlimeNear){
               for(int i = 0;i<stepsDone;i++){
                   System.out.println("X:"+ coordsFound[i][0] * 16 + " Y: " + coordsFound[i][1] * 16 );
               }
               return true;
          }

        return false;

    }
}
