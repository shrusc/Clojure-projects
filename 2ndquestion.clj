;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.
(def bill [{:name "Green tea ice cream" :price 24 :quantity 1} {:price 1.0 :name "Sticky Rice" :quantity 2}] )

(def items [{:name " tea ice cream" :quantity 3 :price 24} {:price 1.0 :name "Mango Rice" :quantity 1}])

(defn check-bill [bill-item item]
(if (and (not (empty? item)) (not (empty? bill-item)))
  (do

  (if (=
          (get bill-item :name)
          (get (first item) :name)
        )
        ( do

             (conj  (update-in bill-item [:quantity] + (get(first item) :quantity) )
						                 (check-bill bill-item (rest item)))


				)

        (check-bill bill-item (rest item))
    )

    )

  )
)
(defn remove-bill [concat-list  x ]
      (if   (empty? x)  concat-list

            (remove-bill     (remove #(=
                                                      (get  (first x) :name)
                                                      (get  % :name)
                                               ) concat-list
                                      )  (rest x)
                  )


      )

  )


(defn merge-test [ a b ]
  (into [](for [x a]

            (check-bill x b)
                )
               )
  )

(defn add-to-bills
 [a b]

 ( remove nil? (concat(remove-bill (concat a b) (merge-test a b))(merge-test a b)))
)





;(check-bill (first bill) items)


 ;(add-to-bills bill items)
(= (get (first bill) :name) (get (first items) :name))

;(remove-bill  (concat bill items) bill-items)





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

(all-names [] bill)

(into ()(all-names [] bill))

(contains? (set (into () (all-names [] bill))) (get (first items) :name))










