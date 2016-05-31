(def app-state
  (atom { :draw-shape {}
         :start {:x "none" :y "none"}
         :end {:x "none" :y "none"}
         :game-status :in-progress}))

(def value (atom {:start {:x 0 :y 0} :end {:x 0 :y 0} :second-click false }))

(:start @app-state)

(def point-schema
 {:x s/Num :y s/Num})

(def line-schema
 {:start point-schema
  :end point-schema})

(def current-mouse-state-schema
  {line-schema :second-click s/Str})

(def event-list-schema
  (abstract-map/abstract-map-schema
   :event))

(abstract-map/extend-schema mode-change event-list-schema [:mode-change] {:last-mode s/Str})
(abstract-map/extend-schema draw event-list-schema [:draw] {:shape s/Str line-schema})


(defn draw-line []
 (doseq [x @app-state]
  [:svg {:width 600 :height 600 :stroke "black"
 :style {:position :fixed :top 0 :left 0 :border "red solid 1px"}}
     [:line
      {:x1 (x :start-x)
       :y1 (x :start-y)
       :x2 (x :end-x)
       :y2 (x :end-y)
       :stroke "red"
       }]]))

(swap! value assoc :start {:x 24 :y 60})

(@value :start :x)


(swap! app-state update-in [:draw-shape] conj
                                 (assoc {} :shape "line"
                                           :x1 (@value :start-x)
                                           :y1 (@value :start-y)
                                           :x2 (@value :end-x)
                                           :y2 (@value :end-y)))
 (swap! app-state update-in [:draw-shape] conj
                                 (assoc {} :shape "line"
                                           :x1 100
                                           :y1 200
                                           :x2 300
                                           :y2 400))

 (swap! value assoc :start {:x 0 :y 0} :end {:x 0 :y 0} :second-click false)

 ;(swap! app-state conj (assoc {} :shape "line"
                                                          ;:start-x (@val :start-x)
                                                          ;:start-y (@val :start-y)
                                                          ;:end-x (@val :end-x)
                                                          ;:end-y (@val :end-y))))}
(last (@app-state :draw-shape))



(when (> (count (@app-state :draw-shape)) 0)
(doseq [x (@app-state :draw-shape)]
  (println (x :x1))))

(defn draw-line []
[:svg {:width 600 :height 600 :stroke "black"
 :style {:position :fixed :top 0 :left 0 :border "red solid 1px"}}
     [:line
      {:x1 (@app-state :start-x)
       :y1 (@app-state :start-y)
       :x2 (@app-state :end-x)
       :y2 (@app-state :end-y)
       :stroke "red"
       }]])


(when (> (count (@app-state :draw-shape)) 0)
      (doseq [x (@app-state :draw-shape)]
      [:line
      {:x1 (- (x :x1) 100)
       :y1 (- (x :y1) 100)
       :x2 (- (x :x2) 100)
       :y2 (- (x :y2) 100)
       :stroke "red"
       }]))


(conj [1 2 3] 4)
(first [1 2 3])

(conj {1 2 3 4} 4 5)
(rest '(4 12 3))

(pop '(1 2 3 4))

(pop [1 2 3 4])

(peek [1 2 3 4])

(defn assignment4 []
  [:center
   [:h1 (:text @app-state)]
    [:svg
     { :width 500
            :height 500
            :on-click
                   (fn mouse-click [e]
                     (if (@value :firstclick)
                      (swap! value assoc :firstclick false :secondclick true :draw true :start-x (.-clientX e)
                             :start-y (.-clientY e) :end-x (.-clientX e) :end-y (.-clientY e))
                       (when (@value :secondclick)
                         (swap! value assoc :secondclick false :firstclick true :end-x (.-clientX e) :end-y (.-clientY e))
                          (swap! app-state update-in [:draw-shape] conj
                                 (assoc {} :shape "line"
                                           :x1 (@value :start-x)
                                           :y1 (@value :start-y)
                                           :x2 (@value :end-x)
                                           :y2 (@value :end-y)))
                         (swap! value assoc :start-x 0 :start-y 0 :end-x 0 :end-y 0))))

           :on-mouse-move
                    (fn mouse-move [e]
                      (when (@value :secondclick)
                        (swap! value assoc :end-x (+ 0 (.-clientX e)) :end-y (+ 0 (.-clientY e)))))
            ;:on-mouse-up #(swap! app-state assoc :update 0 :end-x (.-clientX %) :end-y (.-clientY %))
            :stroke "black"
            :style {:position :fixed :top 100 :left 100 :border "red solid 1px"}}
      (print "lines in app state" (str (count (@app-state :draw-shape))))

    (when (@value :draw)

      [:line {:x1 101
                   :y1 101
                   :x2 200
                   :y2 200}]
      [:line {:x1 300
                   :y1 300
                   :x2 350
                   :y2 350}]
       [:line
      {:x1 (- (@value :start-x) 100)
       :y1 (- (@value :start-y) 100)
       :x2 (- (@value :end-x) 100)
       :y2 (- (@value :end-y) 100)
       :stroke "red"
       }]
      )

     [:rect
      {:x (@value :start-x)
      :y (@value :start-y)
      :width (.abs js/Math (- (@value :end-x) (@value :start-x)))
      :height (.abs js/Math (- (@value :end-y) (@value :start-y)))
      :stroke "red"
       }]



     (print "val x1 y1" (str (@value :start-x) " " (@value :start-y)))
     (print "val x2 y2" (str  (@value :end-x) " " (@value :end-y)))]])
