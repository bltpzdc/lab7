package clientAndServer.tools.insideCommands;

import clientAndServer.startingData.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class UserInput {

    public String getString(String message) {
        FunctionalInputGetter<String> getter = new FunctionalInputGetter<>();
        return getter.parseSomething(Optional::of,message);

    }

    public int getInt(String message) {
        FunctionalInputGetter<Integer> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x) -> {
            try {
                Integer result = Integer.parseInt(x);
                return Optional.of(result);
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public double getDoubleX(String message) {
        FunctionalInputGetter<Double> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x) -> {
            try {
                Double result = Double.parseDouble(x);
                if (result<9) {
                    return Optional.of(result);
                }
                else{
                    System.out.println("This field can not be higher than 8.");
                    return Optional.empty();
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public float getFloatY(String message) {
        FunctionalInputGetter<Float> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x) -> {
            try {
                Float result = Float.parseFloat(x);
                if (result>-986) {
                    return Optional.of(result);
                }
                else{
                    System.out.println("This field can not be lower than -985.");
                    return Optional.empty();
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public Long getLong(String message){
        FunctionalInputGetter<Long> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x) -> {
            try {
                Long result = Long.parseLong(x);
                if (result>0) {
                    return Optional.of(result);
                }
                else{
                    System.out.println("This field can not be lower than 1.");
                    return Optional.empty();
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public MovieGenre getMovieGenre(String message){
        FunctionalInputGetter<MovieGenre> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                MovieGenre enumString = MovieGenre.valueOf(x);
                return Optional.of(enumString);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();

            }
        },message);
    }

    public Date getDate(String message){
        FunctionalInputGetter<Date> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = formatter.parse(x);
                return Optional.of(date);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public MpaaRating getMpaaRating(String message){
        FunctionalInputGetter<MpaaRating> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                MpaaRating enumString = MpaaRating.valueOf(x);
                return Optional.of(enumString);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public EyeColor getEyeColor(String message){
        FunctionalInputGetter<EyeColor> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                EyeColor enumString = EyeColor.valueOf(x);
                return Optional.of(enumString);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public Country getCountry(String message){
        FunctionalInputGetter<Country> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                Country enumString = Country.valueOf(x);
                return Optional.of(enumString);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }

    public HairColor getHairColor(String message){
        FunctionalInputGetter<HairColor> getter = new FunctionalInputGetter<>();
        return getter.parseSomething((x)->{
            try{
                HairColor enumString = HairColor.valueOf(x);
                return Optional.of(enumString);
            }catch (Exception e){
                System.out.println("Invalid value.");
                return Optional.empty();
            }
        },message);
    }
}

class FunctionalInputGetter<T> {
    public T parseSomething(Function<String, Optional<T>> input,String message) {
        boolean isRight = false;
        Scanner scanner = new Scanner(System.in);
        Optional<T> result = Optional.empty();

        while (!isRight) {
            System.out.println(message);
            String tmp = scanner.nextLine();

            result = input.apply(tmp);

            if (result.isPresent()) {
                isRight = true;
            }
        }
        return result.get();
    }
}
