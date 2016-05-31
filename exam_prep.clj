;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(reduce + [])
(reduce * [])

(defn issumorprod [coll x]
  (if (not(number? x)) (number? x)
    (let [sum (reduce + coll)
          prod (reduce * coll)]
      (boolean (or (= x sum) (= x prod))))))

(issumorprod  [2 2 3] 7)
 (issumorprod [2 3] 6)
 (issumorprod [2 3] 4)
 (issumorprod [2 4] "x")

 (merge-with inc [1 2 3])


 (defn sdsu-last [& args]
   (let [[x & more] args]
     (if more (recur more) x)))


 (sdsu-last 5)
 (sdsu-last 5 4 3 2 1)


 (get {:a 1 :b 2} :b 10)

 ({:a 1 :b 2} :b 10)


 (:b {:a 1 :b 2})

 (:b {:a 1 :b 2} 10)

 (first '(1 2 3))
(first [1 2 3])

(peek '(1 2 3))
(peek [1 2 3])
(pop '(1 2 3))
(pop [1 2 3])
(cons :a '(1 2 3))
(cons :a [1 2 3])
(conj '(1 2 3) :a)
(conj [1 2 3] :a)
(number? :a)

(= "cat" "dog")

(= 2 2.0)

(== 2 2.0)

;(nth {4 2} 2)
(get #{3 4} 2)

(let [[a b c & more :as all] [1 2 3 4 5]]
 (println "a b c are:" a b c)
 (println "more is:" all))

(defn height-helper [value tree]
  (if (:value tree) (inc value) value))

(defn height [tree]
  (if (:value tree)
    (let [left-height ( + 1 (height (:left tree)))
          right-height (+ 1 (height (:right tree)))]
    (if (> left-height right-height) (inc left-height) (inc right-height))) 0))

(def tree1 {:value 5 :left: nil :right {:value 10 :left {:value 15 :left nil :right nil} :right nil}})

(height tree1)

(defn reverse-list [l]
  (if (empty? l) '()
    (conj (reverse-list (drop-last l)) (last l))))
(reverse-list '(1 2 3 4 5))
