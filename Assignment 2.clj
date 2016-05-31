;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

;Answer 1

(defn check-divisor [result n]
 (if (= (mod (first result) n) 0) (conj result n) result))

(defn divisors [x]
 ;Since x is a divisor of x putting x in result vector
 (reduce check-divisor (vector x) (range 1 x)))

(divisors 24)

;end Answer 1


;Answer 2
(defn abundance [x]
 (- (reduce + (divisors x)) (* 2 x)))

(abundance 24)

;end Answer 2

;Answer 3
(defn check-abundance [result x]
 (if (> (reduce + (divisors x)) (* 2 x)) (conj result x) result))

(defn find-abundant-nos [n]
 (reduce check-abundance [] (range 1 n)))

(find-abundant-nos 300)

;end Answer 3


;Answer 4
(defn get-count [result text pattern]
 (if (< (count text) (count pattern)) result
  (if (= (subs text 0 (count pattern)) pattern)
   (get-count (inc result) (apply str (rest text)) pattern)
    (get-count result (apply str (rest text)) pattern))))

(defn pattern-count [text pattern]
 (get-count 0 text pattern))

(pattern-count "abababa" "aba")
(pattern-count "aaaaa" "aa")
(pattern-count "Abcde" "Abc")

;end Answer 4

;Answer 5
(defn break-string [string n]
 (if (< (count string) n) '()
  (cons (subs string 0 n) (break-string (apply str (rest string)) n))))

(defn most-frequent-word [string n]
 (let [map (frequencies (break-string string n))
       max (apply max (vals map)) ]
  (filter (comp #{max} map) (keys map))))

(most-frequent-word "TCGAAGCTAGACGCTAGTAGCTAGTGTGCA" 4)
(most-frequent-word "abababa" 2)

;end Answer 5

;Answer 6

(defn find-clumps [text k L t]
 (let [substring-list (break-string text L)]
  (distinct (flatten (reduce
                      (fn [result row]
                       (let [map (frequencies (break-string row k))]
                        (conj result (filter (comp #{t} map) (keys map)))))
                      [] substring-list)))))

(def text
"CGGACTCGACAGATGTGAAGAAATGTGAAGACTGAGTGAAGAGAAGAGGAAACACGACACGACATTGCGACATAATGTACGAATGTAATGTGCCTATGGC")

(find-clumps text 5 75 4)
;end Answer 6

;Answer 7

(defn get-spread [result line]
 (if (empty? line) result
  (let [line (clojure.string/split line #"\s+")
        day (read-string (first line))
        max (read-string (apply str (filter #(Character/isDigit %) (nth line 1))))
        min (read-string (apply str (filter #(Character/isDigit %) (nth line 2))))]
   (conj result (assoc {} day (- max min))))))

(defn maximum-spread [filepath]
 (with-open [rdr (clojure.java.io/reader filepath)]
  (let [spread (apply merge (reduce get-spread [] (drop 2 (line-seq rdr))))
        max-spread (apply max (vals spread))]
    ;Sending the vector of all the days which have the max temp spread
   (vec (filter (comp #{max-spread} spread) (keys spread))))))

(maximum-spread "/Users/adarshkumar/Desktop/clojure-assignments/weather.dat")

;end Answer 7
