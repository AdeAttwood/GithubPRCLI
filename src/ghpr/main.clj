(ns ghpr.main
  (:require 
    [babashka.cli :as cli] 
    [babashka.fs :as fs] 
    [babashka.process :as ps]
    [ghpr.utils :as utils])
  (:gen-class))

(defn prepare-command [_m]
  (if (fs/exists? ".git/GH_PR_BODY.md")
    (utils/edit-file ".git/GH_PR_BODY.md")
    (do
      (if (fs/exists? ".github/pull_request_template.md")
        (fs/copy ".github/pull_request_template.md" ".git/GH_PR_BODY.md")
        (fs/create-file ".git/GH_PR_BODY.md"))
      (utils/edit-file ".git/GH_PR_BODY.md"))))

(defn submit-command [_m]
  (if (fs/exists? ".git/GH_PR_BODY.md")
    (do
      (ps/shell "gh" "pr" "create" "--assignee" "@me" "--body" (slurp ".git/GH_PR_BODY.md"))
      (fs/delete ".git/GH_PR_BODY.md")
      (println "Done!!"))
    (println "There is no PR prepared. Please run the `prepare` command to prepare your PR.")))

(defn help [_m]
  (println "A CLI tool to help create GitHub pull requests using your favourite EDITOR")
  (println "")
  (println "\033[1mCommands:\033[0m")
  (println "  prepare  Prepare you PR description using the project template")
  (println "  submit   Submit your PR using the gh CLI tool")
  (println "  p        Alias to `prepare`")
  (println "  s        Alias to `submit`")
  (println ""))

(def command-table
  [{:cmds ["prepare"] :fn prepare-command}
   {:cmds ["p"] :fn prepare-command}
   {:cmds ["submit"]  :fn submit-command}
   {:cmds ["s"]  :fn submit-command}
   {:cmds []          :fn help}])

(defn -main [& args]
  (cli/dispatch command-table args))
