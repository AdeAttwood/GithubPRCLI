(ns ghpr.main-test
  (:require
   [clojure.test :as t]
   [ghpr.main :as ghpr]
   [ghpr.utils :as utils]
   [babashka.process :as ps]
   [babashka.fs :as fs]))

(def project-root-directory (System/getProperty "user.dir"))

(defn clean-up-fixture [f]
  (f)
  (let [file (str project-root-directory "/.git/GH_PR_BODY.md")]
    (if (fs/exists? file)
      (fs/delete file))))

(t/use-fixtures :each clean-up-fixture)

(t/deftest test-create-pr-body
  (t/testing "The prepare command creates a pr body file"
    (t/is (not (fs/exists? (str project-root-directory "/.git/GH_PR_BODY.md"))))
    (with-redefs [utils/edit-file (fn [_] ())]
      (ghpr/prepare-command ()))
    (t/is (fs/exists? (str project-root-directory "/.git/GH_PR_BODY.md")))))

(t/deftest test-remove-body
  (t/testing "The submit command will remove the file once we are done"
    (fs/create-file ".git/GH_PR_BODY.md")
    (with-redefs [ps/shell (fn [_ _ _ _ _ _ _] ())]
      (ghpr/submit-command ()))
    (t/is (not (fs/exists? (str project-root-directory "/.git/GH_PR_BODY.md"))))))
