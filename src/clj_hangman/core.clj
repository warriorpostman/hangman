(ns clj-hangman.core
  (:gen-class))

(defn random-word 
  "Returns a random word from the file"
  []
  (first
    (shuffle
      (clojure.string/split-lines (slurp "word-list.txt")))))

(defn already-guessed?
  [past-guesses guess]
  (clojure.string/includes? past-guesses guess))

(defn save-guess
  [past-guesses guess]
  (str past-guesses guess))

(defn is-single-char?
  [x]
  (= 1 (count x)))

(defn get-revealed
  "Returns a 'currently revealed' text string (eg. b_a_k s_a_c_)"
  [goal-word past-guesses]
  (apply str 
         (map (fn [x] 
                    (if (clojure.string/includes? past-guesses (str x)) x \_))
                    (seq goal-word))))

(defn get-gallows
  "Returns the gallows image based on a given number of wrong answers"
  [wrong-count]
  (println "   __")
  (println "  |  |")
  (if (< wrong-count 1)
    (println "     |")
    (println "  o  |"))
  (if (< wrong-count 2)
    (println "     |")
    (println "  |  |"))
  (if (< wrong-count 3)
    (println "     |")
    (println " /   |"))
  (println "     |")
  (println "  ___|__")
  )


(defn play-round 
  "Execute logic for each round of game play"
  [goal-word past-guesses]
  (while (< (count past-guesses) 5)
    (
     (println "Guess a letter!")
     (def guess (read-line))
      (if 
        (not (is-single-char? guess))
        ((println "Sorry, you must guess ONE letter at a time")
         (play-round goal-word past-guesses)))
      (if 
        (already-guessed? past-guesses guess)
        ((println "Duplicate guess")
         (play-round goal-word past-guesses))
        ((def new-guesses (save-guess guess past-guesses))
         (get-gallows 0)
         (println "" (get-revealed goal-word new-guesses))
         (play-round goal-word new-guesses)))))
  (println "HTE END"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def current-word (random-word))
  (println "random word: " current-word)
  (println "pretend you don't know the word yet and type a letter okay?")
  (def past-guesses "")
  (play-round current-word past-guesses)
  (println "game over"))

