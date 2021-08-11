/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.deergate.vavr;

import java.util.Optional;
import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import org.junit.Test;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;

/**
 *
 * TODO VavrDemoTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-10  17:04
 *
 */
public class VavrDemoTest {

    @Test
    public void testTuple() {
        var tuple = Tuple.of("java", 8);
        System.out.println(tuple._1);
        System.out.println(tuple._2);

        var that = tuple.map(s -> s.substring(2) + "vr", i -> i / 8);

        System.out.println(that);

        var other = tuple.map((s, i) -> Tuple.of(s + "ya", i / 2));
        System.out.println(other);

        var str = tuple.apply((s, i) -> s.substring(2) + "vr " + i / 8);
        System.out.println(str);
    }

    @Test
    public void funComposition() {
        Function1<Integer, Integer> plusOne = a -> a + 1;
        Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

        Function1<Integer, Integer> add1AndMultiplyByTwo = plusOne.andThen(multiplyByTwo);
        System.out.println(add1AndMultiplyByTwo.apply(2));

        Function1<Integer, Integer> plus1ThenMultiplyByTwo = multiplyByTwo.compose(plusOne);
        System.out.println(plus1ThenMultiplyByTwo.apply(2));
    }

    @Test
    public void funLift() {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);
        var r1 = safeDivide.apply(2, 0);
        System.out.println(r1);

        var r2 = safeDivide.apply(4, 2);
        System.out.println(r2);

        var safeSum = Function2.lift(this::sum);
        var none = safeSum.apply(-1, 2);
        System.out.println(none);
    }

    int sum(int first, int second) {
        if (first < 0 || second < 0) {
            throw new IllegalArgumentException("Only positive integers are allowed");
        }
        return first + second;
    }

    @Test
    public void partialApplication() {
        Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function1<Integer, Integer> add2 = sum.apply(2);
        System.out.println(add2.apply(8));

        Function5<Integer, Integer, Integer, Integer, Integer, Integer> sum5 =
                (a, b, c, d, e) -> a + b + c + d + e;
        Function2<Integer, Integer, Integer> add6 = sum5.apply(2, 3, 1);

        System.out.println(add6.apply(2, 2));
    }

    @Test
    public void currying() {
        Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function1<Integer, Integer> add1 = sum.curried().apply(1);
        System.out.println(add1.apply(2));

        Function4<Integer, Integer, Integer, Integer, Integer> sum4 = (a, b, c, d) -> a + b + c + d;
        Function1<Integer, Function1<Integer, Function1<Integer, Integer>>> plusOne =
                sum4.curried().apply(1);
        System.out.println(plusOne.apply(2).apply(3).apply(4));
    }

    @Test
    public void memoized() {
        Function0<Double> hashCache = Function0.of(Math::random).memoized();
        System.out.println(hashCache.isMemoized());
    }

    public static void main(String[] args) {
        Test2 t = (Test2 & TaggedInterface) () -> 2;
        System.out.println(t instanceof TaggedInterface);
    }

    @Test
    public void optionTest() {
        Optional<String> maybeFoo = Optional.of("Foo");
        System.out.println(maybeFoo.get());

        Optional<String> maybeFooBar =
                maybeFoo.map(s -> (String) null).map(s -> s.toUpperCase() + "bar");

        System.out.println(maybeFooBar.isPresent());

        Option<String> foo = Option.of("Foo");
        System.out.println(foo.get());

        try {
            var bar = foo.map(s -> (String) null).map(s -> s.toUpperCase() + "bar");
            System.out.println(bar.isEmpty());
        } catch (NullPointerException e) {
            System.out.println("NPE!!");
        }

        var safeBar = foo.map(s -> (String) null)
                .flatMap(s -> Option.of(s).map(v -> v.toUpperCase() + "bar"));
        System.out.println(safeBar.isEmpty());

    }

    @Test
    public void lazy() {
        Lazy<Double> lazy = Lazy.of(Math::random);
        System.out.println(lazy.isEvaluated());
        System.out.println(lazy.get());
        System.out.println(lazy.isEvaluated());
        System.out.println(lazy.get());

        CharSequence chars = Lazy.val(() -> "Yay!", CharSequence.class);
        System.out.println(chars);
    }

    @Test
    public void either() {
        Either<Integer, String> either = Either.left(1);
        either = either.map(s -> s.toUpperCase() + "-Right!");
        // either=either.mapLeft(i -> i * 2);
        // System.out.println(either.get());
        System.out.println(either.getLeft());
    }

    @Test
    public void testValid() {
        PersonValidator personValidator = new PersonValidator();

        // Valid(Person(John Doe, 30))
        Validation<Seq<String>, Person> valid = personValidator.validatePerson("John Doe", 30);

        // Invalid(List(Name contains invalid characters: '!4?', Age must be greater than 0))
        Validation<Seq<String>, Person> invalid = personValidator.validatePerson("John? Doe!4", -1);

        System.out.println(valid.isValid());
        System.out.println(valid.get());
        // Exception
        // System.out.println(valid.getError());

        System.out.println(invalid.isValid());
        System.out.println(invalid.isInvalid());
        //java.util.NoSuchElementException: get of 'invalid' Validation
        // System.out.println(invalid.get());
        System.out.println(invalid.getError());
    }

    @Test
    public void testMatch() {
        int i = 1;
        String s = Match(i).of(Case($(1), "one"), Case($(2), "two"), Case($(), "?"));
        System.out.println(s);

        Object obj = Integer.valueOf(123);
        Number plusOne = Match(obj).of(Case($(instanceOf(Integer.class)), j -> j + 1),
                Case($(instanceOf(Double.class)), d -> d + 1), Case($(), o -> {
                    throw new NumberFormatException();
                }));

        String arg = "-h";
        Match(arg).of(
                Case($(isIn("-h", "--help")), o -> run(this::displayHelp)),
                Case($(isIn("-v", "--version")), o -> run(this::displayVersion)),
                Case($(), o -> run(() -> {
                    throw new IllegalArgumentException(arg);
                })));
    }
    
    void displayHelp(){
        
    }
    
    void displayVersion(){
        
    }
}


class PersonValidator {

    private static final String VALID_NAME_CHARS = "[a-zA-Z ]";
    private static final int MIN_AGE = 0;

    public Validation<Seq<String>, Person> validatePerson(String name, int age) {
        return Validation.combine(validateName(name), validateAge(age)).ap(Person::new);
    }

    private Validation<String, String> validateName(String name) {
        return CharSeq.of(name).replaceAll(VALID_NAME_CHARS, "").transform(seq -> seq.isEmpty()
                ? Validation.valid(name)
                : Validation.invalid(
                        "Name contains invalid characters: '" + seq.distinct().sorted() + "'"));
    }

    private Validation<String, Integer> validateAge(int age) {
        return age < MIN_AGE ? Validation.invalid("Age must be at least " + MIN_AGE)
                : Validation.valid(age);
    }

}


class Person {

    public final String name;
    public final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person(" + name + ", " + age + ")";
    }

}


interface Test2 {
    Integer test();
}
