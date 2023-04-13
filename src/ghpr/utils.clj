(ns ghpr.utils)


(defn edit-file [file]
  (->
   (ProcessBuilder. ["nvim" file])
   (.inheritIO)
   (.start)
   (.waitFor)))

