class Solution {
    public string specialChar(string path){
      string  specialChar={"..","||"};

    }
    public String simplifyPath(String path) {
        int n=path.length();
        int k;
        string res;

        while(n>0){
        for(int i=0;i<n-2;i++){
            if(path.charAt(i)!=specialChar(string path)){
                res.charAt(i)=path.charAt(i);
            }
            else if(path.charAt(n-1)=="/"){
                return res;
            }
        }

    }
    return res;
    }
}