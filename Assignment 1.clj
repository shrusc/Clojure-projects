;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(def bill [{:name "Green Tea Ice Cream" :price 2.5 :quantity 2}
 {:price 1.0 :name "Sticky Rice" :quantity 2}])

(def items[{:name "Green Tea Ice Cream" :price 24 :quantity 2}{:price 1.0 :name "Mango Rice" :quantity 2} ])

;Answer to Question 1
(defn bill-total [bill]
  (if (empty? bill) 0
    (+ (* (get (first bill) :price) (get (first bill) :quantity)) (bill-total (rest bill)))))

;End Answer 1


;Answer to Question 2
(defn all-names [result coll]
  (if (empty? coll) result
     (all-names (conj result (get (first coll) :name)) (rest coll))))


(defn format-bill [result item]
  ;check if the item is already present in bill or no
  (if (not(contains? (set (into () (all-names [] result))) (get item :name))) (conj result item)
    (for [x result]
      (if (empty? x) result
        (if (= (get item :name) (get x :name))
          ;remove the already present item and add the updated item values
          (conj (remove #(= (get item :name) (get  % :name)) result)
                (update-in x [:quantity] + (get item :quantity))))))))


(defn add-to-bill [bill items]
(remove nil?(reduce format-bill bill items)))

(add-to-bill bill items)
; End answer 2


;Answer to Question 3
(defn calculate-answer [poly x]
    (if (empty? poly) 0
      (+ (* (reduce * (repeat (second (first poly)) x))
            (first (first poly))) (calculate-answer (rest poly) x))))

(defn make-poly [poly]
  #(calculate-answer poly %) )

(def polynomial (make-poly [[1 2] [3 1] [-1 0]]) )
(polynomial 2)

(map polynomial [0 1 2 3 4 5])

;End Answer 3


;Answer to Question 4
(defn differentiate [poly]
    (if (empty? poly) '()
         (if (= (second (first poly)) 0) (differentiate (rest poly))
           (into [] (cons (vector (* (second (first poly)) (first (first poly))) (dec (second (first poly))))
                          (differentiate (rest poly)))))))

(differentiate [[1 2] [-1 0] [3 1]])

;End Answer 4


;Answer to question 5
(defn compute-x [poly x]
  (float(- x (/ ((make-poly poly) (float x)) ((make-poly (differentiate poly)) x)))))

(defn find-root [difference poly x]
  (if (<= (Math/abs (- x (compute-x poly x))) difference)  x
   (find-root difference poly (compute-x poly x))))

;End Answer 5


; Answer to Question 6
(def account (ref 0))

(defn deposit [account amount]
     (dosync
        (println "Depositing $" amount " into account, balance now: "
            (commute account + amount))))
(defn withdrawal [account amount]
     (dosync
        (println "Withdrawing $" amount " into account, balance now: "
            (commute account - amount))))

(deposit account 100)
(deposit account 50)
(withdrawal account 25)
;End answer 6
