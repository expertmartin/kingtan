# kingtan online store
# GitHub
# Add modified files
  # one file or a list of files sperated with space  
  git add files
  # add all files under the directory
  git add directory 

# Commit
  git commit -m "coments."

# Pull to synchronize with GitHub. passphrase is h****
  git pull

  Example:  
  PS C:\projects\store\github\kingtan> git  pull   
    Enter passphrase for key '/c/Users/tanma/.ssh/id_ed25519': h****     
    remote: Enumerating objects: 1, done.     
    remote: Counting objects: 100% (1/1), done.     
    remote: Total 1 (delta 0), reused 0 (delta 0), pack-reused 0 (from 0)     
    Unpacking objects: 100% (1/1), 908 bytes | 454.00 KiB/s, done.     
    From github.com:expertmartin/kingtan     
       89a9836..43367e3  main       -> origin/main        
     * [new tag]         v0.1       -> v0.1      
    Already up to date.

# Push
  git push

  Example:  
  PS C:\projects\store\github\kingtan> git push  
    Enter passphrase for key '/c/Users/tanma/.ssh/id_ed25519':  
    Enumerating objects: 71, done.  
    Counting objects: 100% (71/71), done.  
    Delta compression using up to 20 threads  
    Compressing objects: 100% (30/30), done.  
    Writing objects: 100% (43/43), 577.62 KiB | 14.44 MiB/s, done.  
    Total 43 (delta 10), reused 0 (delta 0), pack-reused 0  
    remote: Resolving deltas: 100% (10/10), completed with 7 local objects.  
    To github.com:expertmartin/kingtan.git  
    561c99d..07e1e6f  develop -> develop  
  PS C:\projects\store\github\kingtan>
