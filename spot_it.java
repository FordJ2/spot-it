import java.lang.Math;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.*;
import java.time.format.DateTimeFormatter;  

// tutorial vid
// science fair doc disney+

class Main{
    public static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        menu();
        //logic(2, 3);
        //algorithm(4);

        //System.out.println(rand(2, 13));
    }


    //// menu methods

    public static void menu() throws Exception {
        // print info + game data
        delay_print(p_title, 500);
        delay_print(p_rules, 3000);
        delay_print(p_gamemode, 1500);
        int gamemode = int_input(0, 3);
        delay_print(p_difficulty, 1500);
        int difficulty = int_input(0, 3);

        // enter logic gates
        logic(gamemode, difficulty);
    }

    public static void rep_menu() throws Exception {
        bad_cls(5);
        // print game data
        delay_print(p_rules, 1500);
        delay_print(p_gamemode, 1500);
        int gamemode = int_input(0, 3);
        delay_print(p_difficulty, 1500);
        int difficulty = int_input(0, 3);

        // enter logic gates
        logic(gamemode, difficulty);
    }

    public static void farewell() throws Exception {
        System.out.println(":Play again? (1/0)");
        int rep = int_input(0, 1);
        if (rep==1) {
            rep_menu();
        } else {
            System.out.println("\n\nFarewell.\n");
        }
    }


    //// logic methods

    // configure logic gates
    public static void logic(int gamemode, int difficulty) throws Exception {
        int elements=0;
        int cards=0;

        switch (difficulty) {  // tweakable
            case 0:
                elements=4;
                cards=2;
                break;
            case 1:
                elements=6;
                cards=2;
                break;
            case 2:
                elements=8;
                cards=2;
                break;
            case 3:
                // elements
                System.out.println("\nCustom # of elements per card");
                System.out.println(":Enter a number that is 1+ a prime #");
                do {  // while num is not prime
                    elements = int_input();
                } while (!check_prime(elements));
                cards = 2;
                break;
        }

        // compile cardset -- can take a very long time

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  

        System.out.print("\n\nCompiling cardset... -- ");
        System.out.println(dtf.format(now));

        String[][] cardset = algorithm(elements);  // threadable in the future
        System.out.print("Cardset compilation complete. -- ");
        System.out.println(dtf.format(now));
        System.out.println();


        switch (gamemode) {
            case 0:
                zenith(elements, cards, cardset);
                break;
            case 1:
                rapid(elements, cards, cardset);
                break;
            case 2:
                perfection(elements, cards, cardset);
                break;
        }
    }

    public static void zenith(int elements, int cards, String[][] cardset) throws Exception {  // lowest time for 10 cards
        // ez clear screen hehe
        bad_cls(5);


        // submenu
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.printf("\nZenith : (%d elements)(%d cards)\n", elements, cards);
        TimeUnit.MILLISECONDS.sleep(1000);
        int charset_num = elements*(elements-1)+1;
        System.out.printf("Charset : %s\n", Arrays.toString(element_gen(charset_num)).replace(", ", " "));  // q*(q-1)+1
        String[] zenith_obj = {
            "",
            "Objective:",
            "  Spot it for 10 rounds of cards",
            "  Achieve lowest time possible",
            "  No penalty for failure",
            ""
        };
        delay_print(zenith_obj, 1000);

        System.out.print(":Press enter when ready");
        kb.nextLine();
        System.out.println();

        // go!
        long time_start = new Date().getTime();
        int score = 0;
        int score_not = 0;

        do {
            // random arr
            int[] card_index = new int[cards];
            int first = rand(0, charset_num-1);
            int second = rand(0, charset_num-1);

            boolean sorry_its_complecated = true;
            try {
                String[] second_plus = cardset[second+1];
                sorry_its_complecated=true;
            } catch (Exception e) {
                String[] second_minus = cardset[second-1];
                sorry_its_complecated=false;
            }

            card_index[0] = first;
            card_index[1] = (second!=first) ? second : ((sorry_its_complecated) ? second+1 : second-1);

            // print cards
            for (int card_in=0; card_in<card_index.length; card_in++) {
                String[] current_card = cardset[card_index[card_in]];
                String card_in_play = Arrays.toString(current_card).replace(", ", " ");
                System.out.println(card_in + card_in_play);
            }

            // find common char
            String common = "";
            for (String el1: cardset[card_index[0]]) {
                for (String el2: cardset[card_index[1]]) {
                    if (el1.equals(el2)) {
                        common += el1;
                        break;
                    }
                }
            }

            // usr input
            System.out.print("> ");
            //kb.nextLine();  // clear cache?
            String usr_in = kb.nextLine();
            if (!(common.equals(usr_in.trim()))) {
                System.out.println("Incorrect. Keep going!\n");
            } else {
                score += 1;
                System.out.println();
            }
            score_not += 1;
        } while (score<10);

        // calc time
        long time_end = new Date().getTime();
        long time_dif = time_end - time_start;

        // end menu
        System.out.println("\n[Zenith]");
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Score: %d\n", score);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Cards: %d\n", score_not);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Final time: %.2fs\n\n\n", time_dif*0.001);

        farewell();
    }

    public static void rapid(int elements, int cards, String[][] cardset) throws Exception {  // highest score in 30s
        // ez clear screen
        bad_cls(5);

        // submenu
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.printf("\nRapid : (%d elements)(%d cards)\n", elements, cards);
        TimeUnit.MILLISECONDS.sleep(1000);
        int charset_num = elements*(elements-1)+1;
        System.out.printf("Charset : %s\n", Arrays.toString(element_gen(charset_num)).replace(", ", " "));  // q*(q-1)+1
        String[] rapid_obj = {
            "",
            "Objective:",
            "  Spot it for as many rounds as possible",
            "  Time limit of 30 seconds",
            "  No penalty for failure",
            ""
        };
        delay_print(rapid_obj, 1000);

        System.out.print(":Press enter when ready");
        kb.nextLine();
        System.out.println();

        // go!
        long time_start = new Date().getTime();
        int score = 0;
        int score_not = 0;

        do {
            // random arr
            int[] card_index = new int[cards];
            int first = rand(0, charset_num-1);
            int second = rand(0, charset_num-1);

            boolean sorry_its_complecated = true;
            try {
                String[] second_plus = cardset[second+1];
                sorry_its_complecated=true;
            } catch (Exception e) {
                String[] second_minus = cardset[second-1];
                sorry_its_complecated=false;
            }

            card_index[0] = first;
            card_index[1] = (second!=first) ? second : ((sorry_its_complecated) ? second+1 : second-1);

            // print cards
            for (int card_in=0; card_in<card_index.length; card_in++) {
                String[] current_card = cardset[card_index[card_in]];
                String card_in_play = Arrays.toString(current_card).replace(", ", " ");
                System.out.println(card_in + card_in_play);
            }

            // find common char
            String common = "";
            for (String el1: cardset[card_index[0]]) {
                for (String el2: cardset[card_index[1]]) {
                    if (el1.equals(el2)) {
                        common += el1;
                        break;
                    }
                }
            }

            // usr input
            System.out.print("> ");
            //kb.nextLine();  // clear cache?
            String usr_in = kb.nextLine();
            if (!(common.equals(usr_in.trim()))) {
                System.out.println("Incorrect. Keep going!\n");
            } else {
                score += 1;
                System.out.println();
            }
            score_not += 1;
        } while (((new Date().getTime())-time_start)*0.001<30);  // while time passed < 30s

        long time_end = new Date().getTime();
        long time_dif = time_end - time_start;

        // end menu
        System.out.println("\n[Rapid]");
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Score: %d\n", score);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Cards: %d\n", score_not);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Final time: %.0fs\n\n\n", time_dif);

        farewell();
    }

    public static void perfection(int elements, int cards, String[][] cardset) throws Exception {  // highest score until fault
        // ez clear screen
        bad_cls(5);

        // submenu
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.printf("\nPerfection : (%d elements)(%d cards)\n", elements, cards);
        TimeUnit.MILLISECONDS.sleep(1000);
        int charset_num = elements*(elements-1)+1;
        System.out.printf("Charset : %s\n", Arrays.toString(element_gen(charset_num)).replace(", ", " "));  // q*(q-1)+1
        String[] rapid_obj = {
            "",
            "Objective:",
            "  Spot it for as many rounds as possible",
            "  No time limit",
            "  End at failure",
            ""
        };
        delay_print(rapid_obj, 1000);

        System.out.print(":Press enter when ready");
        kb.nextLine();
        System.out.println();

        // go!
        long time_start = new Date().getTime();
        int score = 0;

        do {
            // random arr
            int[] card_index = new int[cards];
            int first = rand(0, charset_num-1);
            int second = rand(0, charset_num-1);

            boolean sorry_its_complecated = true;
            try {
                String[] second_plus = cardset[second+1];
                sorry_its_complecated=true;
            } catch (Exception e) {
                String[] second_minus = cardset[second-1];
                sorry_its_complecated=false;
            }

            card_index[0] = first;
            card_index[1] = (second!=first) ? second : ((sorry_its_complecated) ? second+1 : second-1);

            // print cards
            for (int card_in=0; card_in<card_index.length; card_in++) {
                String[] current_card = cardset[card_index[card_in]];
                String card_in_play = Arrays.toString(current_card).replace(", ", " ");
                System.out.println(card_in + card_in_play);
            }

            // find common char
            String common = "";
            for (String el1: cardset[card_index[0]]) {
                for (String el2: cardset[card_index[1]]) {
                    if (el1.equals(el2)) {
                        common += el1;
                        break;
                    }
                }
            }

            // usr input
            System.out.print("> ");
            //kb.nextLine();  // clear cache?
            String usr_in = kb.nextLine();
            if (!(common.equals(usr_in.trim()))) {
                System.out.println("Incorrect.\n");
                break;
            } else {
                score += 1;
                System.out.println();
            }
        } while (((new Date().getTime())-time_start)*0.001<30);  // while time passed < 30s

        // calc time
        long time_end = new Date().getTime();
        long time_dif = time_end - time_start;

        // end menu
        System.out.println("\n[Perfection]");
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Score: %d\n", score);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.printf("  Final time: %.2fs\n\n\n", time_dif*0.001);

        farewell();
    }


    //// utility methods

    // print out w/ delay
    public static void delay_print (String[] data, long delay) throws InterruptedException {
        for (String line : data) {
            System.out.println(line);
            TimeUnit.MILLISECONDS.sleep(delay);
		}
    }

    // random num
    public static int rand(int min, int max) {
        return ((int)((Math.random()*(max-min+1))+min));
    }

    // int verification
    public static int int_input() {
        int int_num=0;
        boolean stupid;
        do {
            try {
                System.out.print("> ");
                String str_num = kb.nextLine();
                int_num = Integer.parseInt(str_num);
                stupid = false;
            } catch (NumberFormatException e) {
                System.out.println("Invalid. Try again.");
                stupid = true;
            }
        } while (stupid);
        return int_num;
    }

    // int verification w/ min/max
    public static int int_input(int min, int max) {
        int int_num=0;
        boolean stupid;
        do {
            try {
                System.out.print("> ");
                String str_num = kb.nextLine();
                int_num = Integer.parseInt(str_num);
                
                if (int_num>max || int_num<min) {
                    stupid = true;
                } else {
                    stupid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid. Try again.");
                stupid = true;
            }
        } while (stupid);
        return int_num;
    }

    // prime verification
    public static boolean check_prime(int num) {
        num-=1;
        for (int i=1; i<=num; i++) {
            if ((num % i == 0) && (i!=1 && i!=num)) {
                System.out.println("Invalid. Try again.");
                return false;
            }
        }
        return true;
    }
    
    // clear screen but stupid
    public static void bad_cls(int rep) {
        while (rep>0) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n");
            rep--;
        }
    }


    ////////////////// DO NOT ENTER //////////////////
    //// core algorithm methods

    // I went above and beyond the expectations, including this unique, novel idea, that significantly pushed my understanding of java.

    // array element algorithm
    public static String[][] algorithm(int q) {
        
        // vars
        int r = q*(q-1)+1;
        String[] s = element_gen(r);
        String[][] arr = new String[r][q];

        // establish [i][0]
        arr[0][0] = s[0];
        for (int char_m=q-1, x_index=r-1; char_m>=0; char_m--) {  // for each char to be in pos [0] as mchar
            for (int n=q-2; n>=0; n--, x_index--) {  // cycle through letters every q-1th line
                arr[x_index][0] = s[char_m];  // append element to [i][0]
            }
        }

        // establish [0-q][i]
        for (int x_index=0, element=1; element<r-1; x_index++) {
            for (int y_index=1; y_index<=q-1; y_index++, element++) {
                arr[x_index][y_index] = s[element];
            }
        }

        // establish [i][i]
        for (int group=0, shift=0, temp=group; group<=(q-2); group++) {

            // these dont work within the for loop condition
            shift-=shift; // shift=0?
            temp-=temp;
            shift-=group;
            int mod = group*(q-1);

            // for letter
            for (int letter=q; letter<=q+q-2; letter++) {
                
                // for # of times letter should appear
                for (int iter_num=1; iter_num<=q-1; iter_num++) {

                    // retrieve x_index of prev letter
                    int prev=0;
                    for (int x_prev=0; x_prev<=r-1; x_prev++) {
                        for (int y_prev=0; y_prev<=q-1; y_prev++) {
                            if ((arr[x_prev][y_prev]!=null)&&((arr[x_prev][y_prev]).equals(s[letter+mod]))) {
                                //System.out.printf("%d,%d\n", x_prev, y_prev);
                                prev+=x_prev-prev;
                            }
                        }
                    }

                    // establish x_index
                    int end = prev+shift+(q-1);
                    int class_min = ((q-1)*(iter_num))+1;
                    int class_min2 = ((q-1)*(iter_num-1));
                    if ((prev>q-1)&&(group!=0) && ((end<class_min) || (prev==class_min2))) {
                        prev+=q-1;
                    }
                    int x_index = prev+shift+(q-1);

                    // establish y_index
                    int y_index = 0;
                    for (int iy_in=1; iy_in<q; iy_in++) {
                        if (arr[x_index][iy_in]==null) {
                            y_index += iy_in;
                            break;
                        }
                    }

                    // append letter to arr
                    arr[x_index][y_index] = s[letter+mod];
                    shift-=shift+group;
                }

                // cannot include elsewhere
                temp+=1;
                shift+=temp;
            }
        }

        // randomize array
        for (int x_in=0; x_in<=r-1; x_in++)
            for (int y_in=0; y_in<=q-1; y_in++) {
                int swap = rand(0, q-1);
                String temp = arr[x_in][swap];
                arr[x_in][swap] = arr[x_in][y_in];
                arr[x_in][y_in] = temp;
            }

        //print 2d array
        // for (String[] i : arr) {
        //     int index = Arrays.asList(arr).indexOf(i);
        //     System.out.println(Arrays.deepToString(arr[index]).replace(", ", " "));
        // }

        return arr;
    }

    // generate element array
    public static String[] element_gen(int r) {
        String[] element_arr = new String[r];
        for (int i=0; i<r; i++) {
            if (r-33<=126) {  // unicode
                element_arr[i] = String.valueOf((char)(i+33));
            } else {  // digital
                element_arr[i] = String.valueOf(i);
            }
        }
        return element_arr;
    }


    //// resources 

    // as my environment runs on java11, i am unable to use proper textblocks to display the ascii art :/
    public static String[] p_title = {
        "                     __     _ __   ",
        "   _________  ____  / /_   (_) /_  ",
        "  / ___/ __ \\/ __ \\/ __/  / / __/",
        " (__  ) /_/ / /_/ / /_   / / /_    ",
        "/____/ .___/\\____/\\__/  /_/\\__/ ",
        "    /_/ Jonathan Ford    ICS4U",
        "",
        ""
    };

    public static String[] p_rules = {
        "Instructions:",
        "   Spot It consists of 2 factors: elements and cards.",
        "   The goal is to spot the common element between the cards.",
        "   Enter the common element into the terminal. Speed is key.",
        ""
    };

    public static String[] p_gamemode = {
        "",
        "Index >> Gamemode : (Description)",
        "  0 >> Zenith     : (Lowest time for 10 cards)",
        "  1 >> Rapid Fire : (Highest score in 30s)",
        "  2 >> Perfection : (Highest score until fault)",
        "",
        ":Enter desired gamemode index #"
    };

    public static String[] p_difficulty = {
        "",
        "",
        "Index >> Difficulty : (Element number)",
        "  0 >> Easy   : (4)",
        "  1 >> Medium : (6)",
        "  2 >> Hard   : (8)",
        "  3 >> Custom : (?)",
        "",
        ":Enter desired difficulty index #"
    };
}
