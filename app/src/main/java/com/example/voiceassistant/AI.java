package com.example.voiceassistant;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.PatternMatcher;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;


public class AI {
    @TargetApi(Build.VERSION_CODES.O)
    public static void getAnswer(String user_question, final Consumer<String> callback) {


        //Jokes
        final String[] jokes = new String[]{
                "Около трети россиян боятся потерять работу из-за искусственного интеллекта. " +
                        "Зря боятся, никакой интеллект за 15 тысячь работать не будет.",

                "Зачем нам искусственный интеллект? Он у нас и так выглядит неестественным.",

                "- Недавно купил себе утюг с искусственным интеллектом. \n" +
                        "- И как?\n" +
                        "- Реально экономит моё время. Когда я глажу рубашку для работы, он позволяет мне погладить воротничок и грудь, а затем говорит: \"Зачем гладить спину? Под пиджаком всё равно не видно!\" И отключается.",

                "Утюг с интеллектуальной подачей пара\n" +
                        "Недостатки: Гладит плохо\n" +
                        "Достоинства: Прикольно шипит цитатами Гегеля",

                "Искусственный интеллект, созданный в России, первым делом попросил водочки."
        };
        final int n = (int)Math.floor(Math.random() * jokes.length);


        final DateFormat frmt = new SimpleDateFormat("HH:mm");
        frmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        Map<String, String> database = new HashMap<String, String>() {{
            put("привет", "И вам не хворать");
            put("как дела", "Да вроде неплохо");
            put("чем занимаешься", "Отвечаю на глупые вопросы");
            put("как тебя зовут", "Я голосовой помощник - Jarvis");
            put("кто тебя создал", "Михаил");
            put("есть ли жизнь на марсе", "Ни Netflix, ни marvel. Ну разве это жизнь!");
            put("сколько времени", "Сейчас ровно " + frmt.format(new Date()) );

            put("кто сейчас президент россии", "Не знаю, я там не живу");
            put("какого цвета небо", "Вроде с утра было синее");
            put("расскажи анекдот", "" + jokes[n]);
            put("расскажи о себе", "Я высокоразвитый ИИ");
        }};

        user_question = user_question.toLowerCase();

        final ArrayList<String> answers = new ArrayList<>();

        int max_score = 0;
        String max_score_answer = "Ок";
        String[] split_user = user_question.split("\\s+");

        for (String database_question : database.keySet()) {
            database_question = database_question.toLowerCase();
            String[] split_db = database_question.split("\\s+");

            int score = 0;

            for (String word_user : split_user) {
                for (String word_db : split_db) {
                   // int min_len = Math.min(word_db.length(), word_user.length());
                   // int cut_len = (int) (min_len * 0.7);
                   // String word_user_cut = word_user.substring(0, cut_len);
                   // String word_db_cut = word_db.substring(0, cut_len);
                   // if (word_user_cut.equals(word_db_cut)) {
                   //     score++;
                  //  }
                    if (word_user.equals(word_db)) {
                        score++;
                    }
                }
            }
            if (score > max_score) {
                max_score = score;
                max_score_answer = database.get(database_question);
            }
        }

        if (max_score > 0) {
            answers.add(max_score_answer);
        }

        //Weather ----------
        Pattern cityPattern = Pattern.compile("какая погода в городе (\\p{L}+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(user_question);
        if (matcher.find()) {
            String cityName = matcher.group(1);
            Weather.get(cityName, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    answers.add(s);
                    callback.accept(String.join(", ", answers));
                }
            });
        } else {
            if (answers.isEmpty()) {
                callback.accept("Ок");
                return;
            }
            callback.accept(String.join(", ", answers));
        }
        //Quote -------------
        /*
        Quote.get(new Consumer<String>() {
            @Override
            public void accept(String s) {
                answers.add(s);
                callback.accept(String.join(", ", answers));
            }
        });
        */
    }
}
