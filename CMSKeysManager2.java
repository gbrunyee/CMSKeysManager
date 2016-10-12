package CMSKeysManager;

public class CMSKeysManager2
{
	public static void main(String[] args)
	{
		if (args.length != 3)
		{
			System.out.println("Usage: CMSKeysManager2 Dev_File Prod_File Merged_File");
		}
		else
		{
			XMLKeyFile DevFile = new XMLKeyFile();
			DevFile.initXMLKeyFile(args[0]);
			XMLKeyFile ProdFile = new XMLKeyFile();
			ProdFile.initXMLKeyFile(args[1]);

			XMLKeyFile MergedFile = new XMLKeyFile();
			MergedFile.copyXMLKeyFile(ProdFile);
			MergedFile.Merge(DevFile);
			// MergedFile.displayDoc();
			MergedFile.writeFile(args[2]);
		}
	}
}
